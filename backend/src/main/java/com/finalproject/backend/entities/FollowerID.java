package com.finalproject.backend.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class FollowerID {
    private Integer follower;
    private Integer following;

    public FollowerID(Integer follower, Integer following) {
        this.follower = follower;
        this.following = following;
    }

    public FollowerID() {
    }

    public Integer getFollower() {
        return follower;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }
}
