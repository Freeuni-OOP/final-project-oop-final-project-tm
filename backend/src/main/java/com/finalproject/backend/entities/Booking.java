package com.finalproject.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @EmbeddedId
    private BookingID id;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @MapsId("takerId")
    @JoinColumn(name = "taker_id")
    private Users user;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id")
    private Services service;

    public BookingID getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Users getUser() {
        return user;
    }

    public Services getService() {
        return service;
    }
}
