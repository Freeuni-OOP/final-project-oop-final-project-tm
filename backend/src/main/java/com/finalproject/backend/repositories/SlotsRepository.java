package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotsRepository extends JpaRepository<Slot, Integer> {
}
