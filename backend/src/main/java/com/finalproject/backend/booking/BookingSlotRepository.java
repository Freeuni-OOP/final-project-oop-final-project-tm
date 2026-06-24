package com.finalproject.backend.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingSlotRepository extends JpaRepository<BookingSlot, Long> {

    List<BookingSlot> findByServiceIdAndSlotDateTimeBetweenOrderBySlotDateTime(
            Integer serviceId, LocalDateTime start, LocalDateTime end);
}