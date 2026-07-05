package com.finalproject.backend.profile;

import com.finalproject.backend.entities.Follower;
import com.finalproject.backend.entities.FollowerID;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.FollowerRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class FollowService {
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;
    public FollowService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    public int getFollowers(Integer id) {
        System.out.println(id + '\n' + '\n');
        long count = followerRepository.countByFollowID_Following(id);
        return (int)count;
    }

    public int getFollowings(Integer id) {
        long count = followerRepository.countByFollowID_Follower(id);
        return (int)count;
    }

    public void unfollow(Integer followerId, Integer followingId) {
        followerRepository.deleteById(new FollowerID(followerId, followingId));
    }

    public void follow(Integer followerId, Integer followingId) {
        Follower follower1 = new Follower();
        System.out.println("Before \n");
        follower1.setFollower(userRepository.findById(followerId).orElseThrow(()->new RuntimeException("User Not Found")));
        follower1.setFollowing(userRepository.findById(followingId).orElseThrow(()->new RuntimeException("User Not Found")));
        System.out.println("after stuff \n");
        FollowerID id = new FollowerID(followerId, followingId);
        follower1.setFollowID(id);

        followerRepository.save(follower1);
    }
}
