package com.finalproject.backend.repositories;


import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.StarID;
import com.finalproject.backend.entities.Stars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Stars, StarID> {

    int countByStarred(Service starred);
}
