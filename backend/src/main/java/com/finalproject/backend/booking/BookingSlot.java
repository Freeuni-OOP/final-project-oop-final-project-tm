package com.finalproject.backend.booking;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity that maps to the `booking_slots` table created by V2 migration.
 *
 * NOTE: The mock BookingService uses an in-memory map and never persists these
 * objects to the database.  When the DB team is ready, swap the service's
 * in-memory map for calls to {@link BookingSlotRepository}.
 */
@Entity
@Table(name = "booking_slots")
public class BookingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Exact start date and time of this slot. */
    @Column(name = "slot_date_time", nullable = false)
    private LocalDateTime slotDateTime;

    /** Current state of the slot (FREE → PENDING → BOOKED). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status = SlotStatus.FREE;

    /** Populated once a client submits a booking request. */
    @Column(name = "client_name")
    private String clientName;

    /** Populated once a client submits a booking request. */
    @Column(name = "client_email")
    private String clientEmail;

    // ── Constructors ──────────────────────────────────────────────────────────

    public BookingSlot() {}

    public BookingSlot(Long id, LocalDateTime slotDateTime, SlotStatus status) {
        this.id = id;
        this.slotDateTime = slotDateTime;
        this.status = status;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                           { return id; }
    public void setId(Long id)                    { this.id = id; }

    public LocalDateTime getSlotDateTime()                    { return slotDateTime; }
    public void setSlotDateTime(LocalDateTime slotDateTime)   { this.slotDateTime = slotDateTime; }

    public SlotStatus getStatus()                 { return status; }
    public void setStatus(SlotStatus status)      { this.status = status; }

    public String getClientName()                 { return clientName; }
    public void setClientName(String clientName)  { this.clientName = clientName; }

    public String getClientEmail()                { return clientEmail; }
    public void setClientEmail(String email)      { this.clientEmail = email; }
}
