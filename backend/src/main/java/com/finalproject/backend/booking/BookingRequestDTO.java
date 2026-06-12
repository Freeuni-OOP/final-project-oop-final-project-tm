package com.finalproject.backend.booking;

/**
 * Inbound DTO – payload the React frontend POSTs to /api/bookings/request.
 *
 * Expected JSON:
 * {
 *   "slotId":      1,
 *   "clientName":  "Jane Doe",
 *   "clientEmail": "jane.doe@example.com"
 * }
 */
public class BookingRequestDTO {

    private Long   slotId;
    private String clientName;
    private String clientEmail;

    // ── Constructors ──────────────────────────────────────────────────────────

    public BookingRequestDTO() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getSlotId()                    { return slotId; }
    public void setSlotId(Long slotId)         { this.slotId = slotId; }

    public String getClientName()              { return clientName; }
    public void setClientName(String name)     { this.clientName = name; }

    public String getClientEmail()             { return clientEmail; }
    public void setClientEmail(String email)   { this.clientEmail = email; }
}
