package com.finalproject.backend.modules.services;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface listingRepository extends JpaRepository<listing, Integer> {

    @Query("""
        SELECT l FROM listing l
        WHERE
            (:text IS NULL OR
                LOWER(l.title) LIKE LOWER(CONCAT('%', :text, '%')) OR
                LOWER(l.bio)   LIKE LOWER(CONCAT('%', :text, '%')))
        AND (:category IS NULL OR LOWER(l.category) = LOWER(:category))
        AND (:min IS NULL OR l.price >= :min)
        AND (:max IS NULL OR l.price <= :max)
    """)
    List<listing> findByFilters(String text,String category, Double min, Double max, Sort sort);
}
