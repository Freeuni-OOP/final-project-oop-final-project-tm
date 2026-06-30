package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findByProviderId_Id(Integer userId);

    @Query(value = "SELECT u.email FROM slots sl " +
                   "JOIN services s ON sl.service_id = s.service_id " +
                   "JOIN users u ON s.provider_id = u.user_id " +
                   "WHERE sl.slot_id = :slotId",
           nativeQuery = true)
    Optional<String> findOwnerEmailBySlotId(@Param("slotId") Integer slotId);

    @Query(value = "SELECT max_capacity FROM services WHERE service_id = :serviceId",
           nativeQuery = true)
    Optional<Integer> findMaxCapacityByServiceId(@Param("serviceId") Integer serviceId);

    @Query(value = "SELECT s.max_capacity FROM slots sl " +
                   "JOIN services s ON sl.service_id = s.service_id " +
                   "WHERE sl.slot_id = :slotId",
           nativeQuery = true)
    Optional<Integer> findMaxCapacityBySlotId(@Param("slotId") Integer slotId);
}
