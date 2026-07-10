package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

    //pessimistic row lock (FOR UPDATE); only actually locks inside a @Transactional caller
    //this is what requestBooking and blockTime rely on to serialize concurrent bookings for the same service
    @Query(value = "SELECT COALESCE(max_capacity, 1) FROM services WHERE service_id = :serviceId FOR UPDATE",
           nativeQuery = true)
    Optional<Integer> lockAndGetCapacity(@Param("serviceId") Integer serviceId);

    //raw (service_id, capacity) pairs for every service a provider owns
    //used by getUserWeek to build one provider-side layer per service
    @Query(value = "SELECT service_id, COALESCE(max_capacity, 1) FROM services " +
                   "WHERE provider_id = :userId",
           nativeQuery = true)
    List<Object[]> findServiceCapacitiesByProvider(@Param("userId") Integer userId);

    //unlocked read of a service's capacity; empty result signals the service doesn't exist
    //getServiceWeek relies on that emptiness to 404
    @Query(value = "SELECT COALESCE(max_capacity, 1) FROM services WHERE service_id = :serviceId",
           nativeQuery = true)
    Optional<Integer> findMaxCapacityByServiceId(@Param("serviceId") Integer serviceId);

    //who owns a service; drives both the frontend calendar switch and the isOwner check
    @Query(value = "SELECT provider_id FROM services WHERE service_id = :serviceId",
           nativeQuery = true)
    Optional<Integer> findProviderIdByServiceId(@Param("serviceId") Integer serviceId);

    //resolves slot -> service -> provider -> user in one join to get the owner's email
    @Query(value = "SELECT u.email FROM slots sl " +
                   "JOIN services s ON sl.service_id = s.service_id " +
                   "JOIN users u ON s.provider_id = u.user_id " +
                   "WHERE sl.slot_id = :slotId",
           nativeQuery = true)
    Optional<String> findOwnerEmailBySlotId(@Param("slotId") Integer slotId);


    @Query("SELECT s FROM Service s WHERE s.providerId.id = :userId")
    List<Service> findByProviderId(@Param("userId") Integer userId);

    @Query(value = """
           SELECT s.service_id, s.title, s.category, s.price, b.status
           FROM bookings b
           JOIN slots sl ON b.slot_id = sl.slot_id
           JOIN services s ON sl.service_id = s.service_id
           WHERE b.taker_id = :userId
           GROUP BY s.service_id, s.title, s.category, s.price, b.status
    """, nativeQuery = true)
    List<Object[]> findRegisteredServicesByUserId(@Param("userId") Integer userId);
}
