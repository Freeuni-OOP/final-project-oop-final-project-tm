package com.finalproject.backend.repositories;


import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.StarID;
import com.finalproject.backend.entities.Stars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StarRepository extends JpaRepository<Stars, StarID> {
    int countByStarred(Service service);
}
