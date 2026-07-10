package com.finalproject.backend.calendar.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlotStatusTest {

    @Test
    void fromStringParsesKnownValuesIgnoringCaseAndWhitespace() {
        assertThat(SlotStatus.fromString("PENDING")).isEqualTo(SlotStatus.PENDING);
        assertThat(SlotStatus.fromString("booked")).isEqualTo(SlotStatus.BOOKED);
        assertThat(SlotStatus.fromString("  free  ")).isEqualTo(SlotStatus.FREE);
    }

    @Test
    void fromStringFallsBackToFreeForNullOrGarbage() {
        assertThat(SlotStatus.fromString(null)).isEqualTo(SlotStatus.FREE);
        assertThat(SlotStatus.fromString("CONFIRMED")).isEqualTo(SlotStatus.FREE);
        assertThat(SlotStatus.fromString("")).isEqualTo(SlotStatus.FREE);
    }

    @Test
    void higherPicksTheMoreSevereStatus() {
        assertThat(SlotStatus.higher(SlotStatus.FREE, SlotStatus.PENDING)).isEqualTo(SlotStatus.PENDING);
        assertThat(SlotStatus.higher(SlotStatus.PENDING, SlotStatus.BOOKED)).isEqualTo(SlotStatus.BOOKED);
        assertThat(SlotStatus.higher(SlotStatus.BOOKED, SlotStatus.FREE)).isEqualTo(SlotStatus.BOOKED);
        assertThat(SlotStatus.higher(SlotStatus.PENDING, SlotStatus.PENDING)).isEqualTo(SlotStatus.PENDING);
    }

    @Test
    void forCapacityIsFreeBelowCapacity() {
        assertThat(SlotStatus.forCapacity(0, false, 1)).isEqualTo(SlotStatus.FREE);
        assertThat(SlotStatus.forCapacity(1, true, 2)).isEqualTo(SlotStatus.FREE);
    }

    @Test
    void forCapacityAtCapacityIsBookedUnlessAnyPending() {
        assertThat(SlotStatus.forCapacity(1, false, 1)).isEqualTo(SlotStatus.BOOKED);
        assertThat(SlotStatus.forCapacity(2, true, 2)).isEqualTo(SlotStatus.PENDING);
        assertThat(SlotStatus.forCapacity(3, false, 2)).isEqualTo(SlotStatus.BOOKED);
    }
}
