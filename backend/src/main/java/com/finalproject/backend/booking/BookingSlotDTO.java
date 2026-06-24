package com.finalproject.backend.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Outbound DTO – sent to the React frontend to represent one calendar slot.
 *
 * Uses String for status so the frontend receives a plain "FREE" / "PENDING" /
 * "BOOKED" string without needing to know about the Java enum.
 */
public class BookingSlotDTO {

    private Long          id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime slotDateTime;
    private String        status;       // "FREE" | "PENDING" | "BOOKED"

    // ── Constructors ──────────────────────────────────────────────────────────

    public BookingSlotDTO() {}

    public BookingSlotDTO(Long id, LocalDateTime slotDateTime, String status) {
        this.id          = id;
        this.slotDateTime = slotDateTime;
        this.status       = status;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                             { return id; }
    public void setId(Long id)                      { this.id = id; }

    public LocalDateTime getSlotDateTime()                   { return slotDateTime; }
    public void setSlotDateTime(LocalDateTime slotDateTime)  { this.slotDateTime = slotDateTime; }

    public String getStatus()                       { return status; }
    public void setStatus(String status)            { this.status = status; }
}
