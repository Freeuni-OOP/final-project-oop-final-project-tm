package com.finalproject.backend.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "followers")
public class Follower {
    @EmbeddedId
    private FollowerID followID;

    @ManyToOne
    @MapsId("follower")
    @JoinColumn(name = "follower")
    private User follower;

    @ManyToOne
    @MapsId("following")
    @JoinColumn(name = "following")
    private User following;



    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public FollowerID getFollowID() {
        return followID;
    }

    public void setFollowID(FollowerID followID) {
        this.followID = followID;
    }
}
