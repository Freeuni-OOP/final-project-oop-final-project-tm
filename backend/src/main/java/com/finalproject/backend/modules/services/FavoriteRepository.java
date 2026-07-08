package com.finalproject.backend.modules.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT fav FROM Favorite fav WHERE fav.user.userId = :userId")
    List<Favorite> findAllByUserId(Long userId);

    @Query("SELECT COUNT(fav) > 0 FROM Favorite fav WHERE fav.user.userId = :userId AND fav.service.serviceId = :serviceId")
    boolean existsByUserIdAndServiceId(Long userId, Integer serviceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Favorite fav WHERE fav.user.userId = :userId AND fav.service.serviceId = :serviceId")
    void deleteByUserIdAndServiceId(Long userId, Integer serviceId);
}