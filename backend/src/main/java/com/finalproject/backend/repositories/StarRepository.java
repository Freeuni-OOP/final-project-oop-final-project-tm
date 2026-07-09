package com.finalproject.backend.repositories;


import com.finalproject.backend.entities.StarID;
import com.finalproject.backend.entities.Stars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StarRepository extends JpaRepository<Stars, StarID> {
    @Query("SELECT COUNT(s) > 0 FROM Stars s WHERE s.starID.starer = :userId AND s.starID.starred = :serviceId")
    boolean existsByUserIdAndServiceId(@Param("userId") int userId, @Param("serviceId") int serviceId);
}
