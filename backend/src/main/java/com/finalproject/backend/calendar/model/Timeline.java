package com.finalproject.backend.calendar.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class Timeline {

    private Timeline() {}

    //one raw booked interval (pre-merge), as pulled straight from a booking
    public record Busy(LocalDateTime start, LocalDateTime end, boolean pending) {}

    //a merged interval with its resolved status — the output of segments()/overlay()
    public record Segment(LocalDateTime start, LocalDateTime end, SlotStatus status) {}

    //sweep-line point used only by maxConcurrency: +1 at a Busy's start, -1 at its end
    private record Event(LocalDateTime time, int delta) {}

    //slices the timeline at every busy intervals start/end
    //resolves each slice's status by capacity
    //then merges adjacent same-status slices
    public static List<Segment> segments(List<Busy> busy, int capacity) {
        if (busy.isEmpty()) return List.of();

        List<LocalDateTime> points = boundaries(busy);
        List<Segment> raw = new ArrayList<>();

        for (int i = 0; i + 1 < points.size(); i++) {
            LocalDateTime from = points.get(i);
            LocalDateTime to = points.get(i + 1);

            int count = 0;
            boolean anyPending = false;
            for (Busy b : busy) {
                if (!b.start().isAfter(from) && !b.end().isBefore(to)) {
                    count++;
                    if (b.pending()) anyPending = true;
                }
            }

            SlotStatus status = SlotStatus.forCapacity(count, anyPending, capacity);
            if (status != SlotStatus.FREE) raw.add(new Segment(from, to, status));
        }

        return merge(raw);
    }

    //pools boundaries from every layer
    //then for each slice picks the highest-severity status among layers that fully cover it
    public static List<Segment> overlay(List<List<Segment>> layers) {
        List<Segment> all = new ArrayList<>();
        for (List<Segment> layer : layers) all.addAll(layer);
        if (all.isEmpty()) return List.of();

        TreeSet<LocalDateTime> bounds = new TreeSet<>();
        for (Segment s : all) {
            bounds.add(s.start());
            bounds.add(s.end());
        }
        List<LocalDateTime> points = new ArrayList<>(bounds);
        List<Segment> raw = new ArrayList<>();

        for (int i = 0; i + 1 < points.size(); i++) {
            LocalDateTime from = points.get(i);
            LocalDateTime to = points.get(i + 1);

            SlotStatus status = SlotStatus.FREE;
            for (Segment s : all) {
                if (!s.start().isAfter(from) && !s.end().isBefore(to)) {
                    status = SlotStatus.higher(status, s.status());
                }
            }
            if (status != SlotStatus.FREE) raw.add(new Segment(from, to, status));
        }

        return merge(raw);
    }

    //clips busy intervals to [from, to)
    //sweeps +1/-1 events, ties sorted end-before-start
    //so a booking ending exactly when another starts isn't counted as overlapping
    public static int maxConcurrency(List<Busy> busy, LocalDateTime from, LocalDateTime to) {
        List<Event> events = new ArrayList<>();
        for (Busy b : busy) {
            LocalDateTime start = b.start().isBefore(from) ? from : b.start();
            LocalDateTime end = b.end().isAfter(to) ? to : b.end();
            if (!start.isBefore(end)) continue;
            events.add(new Event(start, 1));
            events.add(new Event(end, -1));
        }

        events.sort((a, b) -> {
            int c = a.time().compareTo(b.time());
            return c != 0 ? c : Integer.compare(a.delta(), b.delta());
        });

        int running = 0;
        int max = 0;
        for (Event e : events) {
            running += e.delta();
            max = Math.max(max, running);
        }
        return max;
    }

    //collects every busy start/end into a sorted, deduplicated list
    //these become the slice boundaries that segments() walks
    private static List<LocalDateTime> boundaries(List<Busy> busy) {
        TreeSet<LocalDateTime> bounds = new TreeSet<>();
        for (Busy b : busy) {
            bounds.add(b.start());
            bounds.add(b.end());
        }
        return new ArrayList<>(bounds);
    }

    //merges adjacent segments into one when they share a status and touch exactly at the boundary
    private static List<Segment> merge(List<Segment> raw) {
        List<Segment> out = new ArrayList<>();
        for (Segment s : raw) {
            if (!out.isEmpty()) {
                Segment last = out.get(out.size() - 1);
                if (last.status() == s.status() && last.end().equals(s.start())) {
                    out.set(out.size() - 1, new Segment(last.start(), s.end(), s.status()));
                    continue;
                }
            }
            out.add(s);
        }
        return out;
    }
}
