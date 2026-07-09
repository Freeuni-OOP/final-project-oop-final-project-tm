package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.FollowerID;
import com.finalproject.backend.entities.Follower;
import com.finalproject.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, FollowerID> {
    long countByFollowID_Follower(Integer followerId);
    long countByFollowID_Following(Integer followingId);
}
