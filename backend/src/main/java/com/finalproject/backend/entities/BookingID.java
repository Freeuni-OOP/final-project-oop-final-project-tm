package com.finalproject.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookingID implements Serializable {
    private int takerId;
    private int slotId;

    public BookingID() {

    }

    public BookingID(int takerId, int slotId) {
        this.takerId = takerId;
        this.slotId = slotId;
    }

    public void setTakerId(int takerId) {
        this.takerId = takerId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getTakerId() {
        return takerId;
    }

    public int getSlotId() {
        return slotId;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        BookingID tmp = (BookingID) o;
        return takerId == tmp.takerId && slotId == tmp.slotId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(takerId, slotId);
    }
}
