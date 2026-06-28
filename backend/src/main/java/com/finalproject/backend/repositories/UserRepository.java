package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // has:  findById, findAll, count, deleteById etc
    Optional<User> findByEmail(String email);
}
