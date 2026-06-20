package com.finalproject.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class BookingID implements Serializable {
    private int takerId;
    private int serviceId;

    public BookingID() {

    }

    public BookingID(int takerId, int serviceId) {
        this.takerId = takerId;
        this.serviceId = serviceId;
    }

    public int getTakerId() {
        return takerId;
    }

    public int getServiceId() {
        return serviceId;
    }
}
