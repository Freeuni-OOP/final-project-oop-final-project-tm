package com.finalproject.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;

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
}
