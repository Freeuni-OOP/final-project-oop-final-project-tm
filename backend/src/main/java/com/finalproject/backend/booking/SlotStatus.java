package com.finalproject.backend.booking;

/**
 * Represents the lifecycle state of a single booking slot.
 *
 * FREE    – No one has claimed this slot; it is clickable on the calendar.
 * PENDING – A client submitted a request; awaiting clinic-owner confirmation.
 * BOOKED  – The clinic owner confirmed the appointment.
 */
public enum SlotStatus {
    FREE,
    PENDING,
    BOOKED
}
