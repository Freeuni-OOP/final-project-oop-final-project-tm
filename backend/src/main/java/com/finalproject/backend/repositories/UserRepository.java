package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    // has:  findById, findAll, count, deleteById etc
    Optional<Users> findByEmail(String email);
}
