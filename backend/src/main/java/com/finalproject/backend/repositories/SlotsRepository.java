package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

//no custom queries needed: Slot is properly @Table-mapped, so inherited
//save()/deleteById() from JpaRepository work correctly, unlike ServiceRepository
public interface SlotsRepository extends JpaRepository<Slot, Integer> {
}
