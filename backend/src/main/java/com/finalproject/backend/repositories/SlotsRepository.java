package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotsRepository extends JpaRepository<Slot, Integer> {

    List<Slot> findByServiceId_IdAndStartTimeBetweenOrderByStartTime(
            Integer serviceId, LocalDateTime start, LocalDateTime end);
}
