package com.finalproject.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "stars")
public class Stars {
    @EmbeddedId
    private StarID starID;

    @ManyToOne
    @MapsId("starer")
    @JoinColumn(name = "starer")
    private User starer;

    @ManyToOne
    @MapsId("starred")
    @JoinColumn(name = "starred")
    private Service starred;

    public StarID getStarID() {
        return starID;
    }

    public User getStarer() {
        return starer;
    }

    public Service getStarred() {
        return starred;
    }

    public void setStarID(StarID starID) {
        this.starID = starID;
    }

    public void setStarer(User starer) {
        this.starer = starer;
    }

    public void setStarred(Service starred) {
        this.starred = starred;
    }

    public Stars() {
    }

    public Stars(StarID starID, User starer, Service starred) {
        this.starID = starID;
        this.starer = starer;
        this.starred = starred;
    }
}
