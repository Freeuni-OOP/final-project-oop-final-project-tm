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
    private User user;

    @ManyToOne
    @MapsId("slotId")
    @JoinColumn(name = "slot_id")
    private Slot slot;

    public void setId(BookingID id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public BookingID getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public Slot getSlot() {
        return slot;
    }
}
