package com.finalproject.backend.calendar.model;

public enum SlotStatus {
    FREE,
    PENDING,
    BOOKED;

    public static SlotStatus fromString(String value) {
        if (value == null) return FREE;
        try {
            return SlotStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return FREE;
        }
    }

    public static SlotStatus higher(SlotStatus a, SlotStatus b) {
        return a.ordinal() >= b.ordinal() ? a : b;
    }

    public static SlotStatus forCapacity(int occupied, boolean anyPending, int capacity) {
        if (occupied < capacity) return FREE;
        return anyPending ? PENDING : BOOKED;
    }
}
