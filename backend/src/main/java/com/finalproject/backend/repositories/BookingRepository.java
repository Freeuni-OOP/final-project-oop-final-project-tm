package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.BookingID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, BookingID> {

    List<Booking> findBySlot_SlotIdIn(List<Integer> slotIds);

    long countBySlot_SlotId(Integer slotId);
}
