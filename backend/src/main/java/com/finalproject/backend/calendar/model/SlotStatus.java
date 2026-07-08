package com.finalproject.backend.calendar.model;

public enum SlotStatus {
    FREE,
    PENDING,
    BOOKED;

    //defensively parses a raw status string from the DB
    //null or any unrecognized value falls back to FREE
    public static SlotStatus fromString(String value) {
        if (value == null) return FREE;
        try {
            return SlotStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return FREE;
        }
    }

    //relies on declaration order as a severity ranking (FREE < PENDING < BOOKED)
    //used to resolve overlapping segments to the more severe status
    public static SlotStatus higher(SlotStatus a, SlotStatus b) {
        return a.ordinal() >= b.ordinal() ? a : b;
    }

    //non-FREE only once occupied reaches capacity; a pending booking outranks confirmed ones
    public static SlotStatus forCapacity(int occupied, boolean anyPending, int capacity) {
        if (occupied < capacity) return FREE;
        return anyPending ? PENDING : BOOKED;
    }
}
