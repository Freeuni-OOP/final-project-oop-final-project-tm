package com.finalproject.backend.modules.services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface listingRepository extends JpaRepository<listing, Integer> {
    List<listing> findByTitleContainingIgnoreCase(String title);
    List<listing> findByPriceBetween(Double min, Double max);
    List<listing> findByCategory(String category);
}