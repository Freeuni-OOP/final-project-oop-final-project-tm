package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.BookingID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, BookingID> {

    //bookings on a given service within [start, end], provider-side view
    @Query(value = "SELECT b.* FROM bookings b " +
                   "JOIN slots s ON b.slot_id = s.slot_id " +
                   "WHERE s.service_id = :serviceId AND s.start_time BETWEEN :start AND :end",
           nativeQuery = true)
    List<Booking> findForServiceBetween(@Param("serviceId") Integer serviceId,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    //bookings the given user made as a customer within [start, end]
    //customer-side counterpart to findForServiceBetween, used to show a user's own reservations
    @Query(value = "SELECT b.* FROM bookings b " +
                   "JOIN slots s ON b.slot_id = s.slot_id " +
                   "WHERE b.taker_id = :takerId AND s.start_time BETWEEN :start AND :end",
           nativeQuery = true)
    List<Booking> findForTakerBetween(@Param("takerId") Integer takerId,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    //derived query, safe here since Booking is properly @Table-mapped (unlike Service)
    //used by rejectBooking to check if a slot has no bookings left before deleting it
    long countBySlot_SlotId(Integer slotId);
}
