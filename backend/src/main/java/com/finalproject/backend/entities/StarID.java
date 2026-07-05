package com.finalproject.backend.entities;

import jakarta.persistence.Embeddable;


@Embeddable
public class StarID {
    private Integer starer;
    private Integer starred;

    public StarID(Integer starer, Integer starred) {
        this.starer = starer;
        this.starred = starred;
    }

    public StarID() {
    }

    public Integer getStarer() {
        return starer;
    }

    public Integer getStarred() {
        return starred;
    }

    public void setStarer(Integer starer) {
        this.starer = starer;
    }

    public void setStarred(Integer starred) {
        this.starred = starred;
    }
}
