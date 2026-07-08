package com.finalproject.backend.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class StarID implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        StarID tmp = (StarID) o;
        return starer.equals(tmp.starer) && starred.equals(tmp.starred);
    }

    @Override
    public int hashCode() {
        return Objects.hash(starer, starred);
    }
}
