package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.BookingID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, BookingID> {

}
