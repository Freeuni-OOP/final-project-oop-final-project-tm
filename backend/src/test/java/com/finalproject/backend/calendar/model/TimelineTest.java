package com.finalproject.backend.calendar.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TimelineTest {

    private static LocalDateTime at(int hour, int minute) {
        return LocalDateTime.of(2026, 7, 6, hour, minute);
    }

    @Test
    void segmentsOfNothingIsEmpty() {
        assertThat(Timeline.segments(List.of(), 1)).isEmpty();
    }

    @Test
    void singleBookingAtCapacityOneBecomesBookedSegment() {
        List<Timeline.Busy> busy = List.of(new Timeline.Busy(at(10, 0), at(12, 0), false));

        List<Timeline.Segment> segments = Timeline.segments(busy, 1);

        assertThat(segments).containsExactly(
                new Timeline.Segment(at(10, 0), at(12, 0), SlotStatus.BOOKED));
    }

    @Test
    void pendingBookingAtCapacityBecomesPendingSegment() {
        List<Timeline.Busy> busy = List.of(new Timeline.Busy(at(10, 0), at(11, 0), true));

        List<Timeline.Segment> segments = Timeline.segments(busy, 1);

        assertThat(segments).containsExactly(
                new Timeline.Segment(at(10, 0), at(11, 0), SlotStatus.PENDING));
    }

    @Test
    void bookingBelowCapacityStaysFreeAndProducesNoSegment() {
        List<Timeline.Busy> busy = List.of(new Timeline.Busy(at(10, 0), at(12, 0), false));

        assertThat(Timeline.segments(busy, 2)).isEmpty();
    }

    @Test
    void onlyTheOverlapReachesCapacity() {
        List<Timeline.Busy> busy = List.of(
                new Timeline.Busy(at(10, 0), at(12, 0), false),
                new Timeline.Busy(at(11, 0), at(13, 0), false));

        List<Timeline.Segment> segments = Timeline.segments(busy, 2);

        assertThat(segments).containsExactly(
                new Timeline.Segment(at(11, 0), at(12, 0), SlotStatus.BOOKED));
    }

    @Test
    void pendingOutranksBookedInsideTheOverlap() {
        List<Timeline.Busy> busy = List.of(
                new Timeline.Busy(at(10, 0), at(12, 0), true),
                new Timeline.Busy(at(11, 0), at(13, 0), false));

        List<Timeline.Segment> segments = Timeline.segments(busy, 2);

        assertThat(segments).containsExactly(
                new Timeline.Segment(at(11, 0), at(12, 0), SlotStatus.PENDING));
    }

    @Test
    void adjacentSameStatusSegmentsAreMerged() {
        List<Timeline.Busy> busy = List.of(
                new Timeline.Busy(at(10, 0), at(11, 0), false),
                new Timeline.Busy(at(11, 0), at(12, 0), false));

        List<Timeline.Segment> segments = Timeline.segments(busy, 1);

        assertThat(segments).containsExactly(
                new Timeline.Segment(at(10, 0), at(12, 0), SlotStatus.BOOKED));
    }

    @Test
    void adjacentSegmentsWithDifferentStatusStaySeparate() {
        List<Timeline.Busy> busy = List.of(
                new Timeline.Busy(at(10, 0), at(11, 0), false),
                new Timeline.Busy(at(11, 0), at(12, 0), true));

        List<Timeline.Segment> segments = Timeline.segments(busy, 1);

        assertThat(segments).containsExactly(
                new Timeline.Segment(at(10, 0), at(11, 0), SlotStatus.BOOKED),
                new Timeline.Segment(at(11, 0), at(12, 0), SlotStatus.PENDING));
    }

    @Test
    void overlayOfNothingIsEmpty() {
        assertThat(Timeline.overlay(List.of())).isEmpty();
        assertThat(Timeline.overlay(List.of(List.of()))).isEmpty();
    }

    @Test
    void overlayPicksTheMoreSevereStatusWhereLayersOverlap() {
        List<Timeline.Segment> pendingLayer = List.of(
                new Timeline.Segment(at(10, 0), at(11, 0), SlotStatus.PENDING));
        List<Timeline.Segment> bookedLayer = List.of(
                new Timeline.Segment(at(10, 30), at(11, 30), SlotStatus.BOOKED));

        List<Timeline.Segment> result = Timeline.overlay(List.of(pendingLayer, bookedLayer));

        assertThat(result).containsExactly(
                new Timeline.Segment(at(10, 0), at(10, 30), SlotStatus.PENDING),
                new Timeline.Segment(at(10, 30), at(11, 30), SlotStatus.BOOKED));
    }

    @Test
    void overlayKeepsDisjointSegmentsFromDifferentLayers() {
        List<Timeline.Segment> morning = List.of(
                new Timeline.Segment(at(9, 0), at(10, 0), SlotStatus.BOOKED));
        List<Timeline.Segment> afternoon = List.of(
                new Timeline.Segment(at(14, 0), at(15, 0), SlotStatus.PENDING));

        List<Timeline.Segment> result = Timeline.overlay(List.of(morning, afternoon));

        assertThat(result).containsExactly(
                new Timeline.Segment(at(9, 0), at(10, 0), SlotStatus.BOOKED),
                new Timeline.Segment(at(14, 0), at(15, 0), SlotStatus.PENDING));
    }

    @Test
    void maxConcurrencyCountsOverlappingBookings() {
        List<Timeline.Busy> busy = List.of(
                new Timeline.Busy(at(10, 0), at(12, 0), false),
                new Timeline.Busy(at(11, 0), at(13, 0), false));

        assertThat(Timeline.maxConcurrency(busy, at(0, 0), at(23, 59))).isEqualTo(2);
    }

    @Test
    void backToBackBookingsDoNotCountAsOverlapping() {
        List<Timeline.Busy> busy = List.of(
                new Timeline.Busy(at(10, 0), at(11, 0), false),
                new Timeline.Busy(at(11, 0), at(12, 0), false));

        assertThat(Timeline.maxConcurrency(busy, at(0, 0), at(23, 59))).isEqualTo(1);
    }

    @Test
    void maxConcurrencyClipsToTheRequestedWindow() {
        List<Timeline.Busy> busy = List.of(
                new Timeline.Busy(at(9, 0), at(17, 0), false),
                new Timeline.Busy(at(18, 0), at(19, 0), false));

        assertThat(Timeline.maxConcurrency(busy, at(10, 0), at(11, 0))).isEqualTo(1);
        assertThat(Timeline.maxConcurrency(busy, at(17, 0), at(18, 0))).isEqualTo(0);
    }

    @Test
    void maxConcurrencyOfNothingIsZero() {
        assertThat(Timeline.maxConcurrency(List.of(), at(0, 0), at(23, 59))).isEqualTo(0);
    }

    @Test
    void twoIdenticalBookingsFillCapacityTwo() {
        List<Timeline.Busy> busy = List.of(
                new Timeline.Busy(at(10, 0), at(11, 0), false),
                new Timeline.Busy(at(10, 0), at(11, 0), false));

        assertThat(Timeline.segments(busy, 2)).containsExactly(
                new Timeline.Segment(at(10, 0), at(11, 0), SlotStatus.BOOKED));
        assertThat(Timeline.maxConcurrency(busy, at(0, 0), at(23, 59))).isEqualTo(2);
    }

    @Test
    void zeroLengthBusyIntervalContributesNothing() {
        List<Timeline.Busy> busy = List.of(new Timeline.Busy(at(10, 0), at(10, 0), false));

        assertThat(Timeline.maxConcurrency(busy, at(0, 0), at(23, 59))).isEqualTo(0);
    }
}
