package com.finalproject.backend.repositories;
import com.finalproject.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserModuleRepository extends JpaRepository<User, Integer> {

}