package com.finalproject.backend.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for {@link BookingSlot}.
 *
 * TODO (DB team): Add custom query methods here as needed, e.g.
 *   List<BookingSlot> findBySlotDateTimeBetween(LocalDateTime from, LocalDateTime to);
 *   List<BookingSlot> findByStatus(SlotStatus status);
 *
 * The mock BookingService does NOT call this repository yet.
 * When the DB is ready, inject this repository into BookingService and
 * replace the in-memory map calls with repository calls.
 */
@Repository
public interface BookingSlotRepository extends JpaRepository<BookingSlot, Long> {
}
