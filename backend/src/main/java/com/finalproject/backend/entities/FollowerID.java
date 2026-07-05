package com.finalproject.backend.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FollowerID implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        FollowerID tmp = (FollowerID) o;
        return follower.equals(tmp.follower) && following.equals(tmp.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }
}
