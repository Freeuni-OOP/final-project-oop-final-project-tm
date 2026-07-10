package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface listingRepository extends JpaRepository<Service, Integer> {

    @Query("""
        SELECT l FROM Service l
        WHERE 
            (:text IS NULL OR 
                LOWER(l.title) LIKE LOWER(CONCAT('%', :text, '%')) OR 
                LOWER(l.bio)   LIKE LOWER(CONCAT('%', :text, '%')))
        AND (:category IS NULL OR LOWER(l.category) = LOWER(:category))
        AND (:min IS NULL OR l.price >= :min)
        AND (:max IS NULL OR l.price <= :max)
        AND (:providerId IS NULL OR l.providerId.id = :providerId)
        AND (:favoriteUserId IS NULL OR EXISTS (
                SELECT f FROM Favorite f 
                WHERE f.service.id = l.id 
                AND f.user.id = :favoriteUserId
            ))
        AND (:excludeFavUserId IS NULL OR NOT EXISTS (
                SELECT f FROM Favorite f 
                WHERE f.service.id = l.id 
                AND f.user.id = :excludeFavUserId
            ))
    """)
    List<Service> findByFilters(String text, String category, Double min, Double max, Long providerId, Long favoriteUserId, Long excludeFavUserId, Sort sort);
}
