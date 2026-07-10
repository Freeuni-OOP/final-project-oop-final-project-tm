package com.finalproject.backend.repositories;

import com.finalproject.backend.modules.services.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    @Query("SELECT fav FROM Favorite fav WHERE fav.user.id = :userId")
    List<Favorite> findAllByUserId(Integer userId);

    @Query("SELECT COUNT(fav) > 0 FROM Favorite fav WHERE fav.user.id = :userId AND fav.service.id = :serviceId")
    boolean existsByUserIdAndServiceId(Integer userId, Integer serviceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Favorite fav WHERE fav.user.id = :userId AND fav.service.id = :serviceId")
    void deleteByUserIdAndServiceId(Integer userId, Integer serviceId);
}