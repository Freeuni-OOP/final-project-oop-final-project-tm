package com.finalproject.backend.calendar.services;

import com.finalproject.backend.calendar.dtos.DayColumnDTO;
import com.finalproject.backend.calendar.dtos.SegmentDTO;
import com.finalproject.backend.calendar.dtos.WeekCalendarDTO;
import com.finalproject.backend.calendar.model.CalendarHours;
import com.finalproject.backend.calendar.model.SlotStatus;
import com.finalproject.backend.calendar.model.Timeline;
import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.repositories.BookingRepository;
import com.finalproject.backend.repositories.ServiceRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CalendarService {

    private final ServiceRepository serviceRepository;
    private final BookingRepository bookingRepository;

    public CalendarService(ServiceRepository serviceRepository,
                           BookingRepository bookingRepository) {
        this.serviceRepository = serviceRepository;
        this.bookingRepository = bookingRepository;
    }

    //returns empty only when the service itself doesn't exist, letting the controller 404
    //an existing service with no bookings still returns a populated (empty) week
    @Transactional(readOnly = true)
    public Optional<WeekCalendarDTO> getServiceWeek(Integer serviceId, int weekOffset) {
        Optional<Integer> maxCapacity = serviceRepository.findMaxCapacityByServiceId(serviceId);
        if (maxCapacity.isEmpty()) {
            return Optional.empty();
        }
        int capacity = maxCapacity.get();
        LocalDate monday = mondayOf(weekOffset);

        List<Booking> bookings = bookingRepository
                .findForServiceBetween(serviceId, weekStart(monday), weekEnd(monday));
        Map<LocalDate, List<Timeline.Busy>> busyByDay = bucketBusy(bookings);

        List<DayColumnDTO> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            List<Timeline.Busy> busy = busyByDay.getOrDefault(day, List.of());
            days.add(toDayColumn(day, Timeline.segments(busy, capacity)));
        }
        return Optional.of(buildWeek(monday, days));
    }

    //combines provider layers (per owned service) with a customer layer for the user's own bookings
    //customer layer always uses capacity 1, since a reservation fully occupies the user regardless of the service's capacity
    @Transactional(readOnly = true)
    public WeekCalendarDTO getUserWeek(Integer userId, int weekOffset) {
        LocalDate monday = mondayOf(weekOffset);
        Map<LocalDate, List<List<Timeline.Segment>>> layersByDay = new HashMap<>();

        for (Object[] row : serviceRepository.findServiceCapacitiesByProvider(userId)) {
            int serviceId = ((Number) row[0]).intValue();
            int capacity = ((Number) row[1]).intValue();

            List<Booking> bookings = bookingRepository
                    .findForServiceBetween(serviceId, weekStart(monday), weekEnd(monday));
            Map<LocalDate, List<Timeline.Busy>> busyByDay = bucketBusy(bookings);

            for (Map.Entry<LocalDate, List<Timeline.Busy>> entry : busyByDay.entrySet()) {
                List<Timeline.Segment> segs = Timeline.segments(entry.getValue(), capacity);
                if (!segs.isEmpty()) {
                    layersByDay.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(segs);
                }
            }
        }

        List<Booking> ownBookings = bookingRepository
                .findForTakerBetween(userId, weekStart(monday), weekEnd(monday));
        for (Map.Entry<LocalDate, List<Timeline.Busy>> entry : bucketBusy(ownBookings).entrySet()) {
            List<Timeline.Segment> segs = Timeline.segments(entry.getValue(), 1);
            if (!segs.isEmpty()) {
                layersByDay.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(segs);
            }
        }

        List<DayColumnDTO> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            List<List<Timeline.Segment>> layers = layersByDay.getOrDefault(day, List.of());
            days.add(toDayColumn(day, Timeline.overlay(layers)));
        }
        return buildWeek(monday, days);
    }

    //groups bookings by the calendar day of their slot's start time, converting each to a Timeline.Busy
    //skips bookings with incomplete slot data instead of failing
    //any status that isn't exactly PENDING is treated as booked, including malformed values
    private Map<LocalDate, List<Timeline.Busy>> bucketBusy(List<Booking> bookings) {
        Map<LocalDate, List<Timeline.Busy>> byDay = new HashMap<>();
        for (Booking booking : bookings) {
            Slot slot = booking.getSlot();
            if (slot == null || slot.getStartTime() == null || slot.getEndTime() == null) continue;
            boolean pending = SlotStatus.fromString(booking.getStatus()) == SlotStatus.PENDING;
            Timeline.Busy busy = new Timeline.Busy(slot.getStartTime(), slot.getEndTime(), pending);
            byDay.computeIfAbsent(slot.getStartTime().toLocalDate(), k -> new ArrayList<>()).add(busy);
        }
        return byDay;
    }

    //clamps each segment into the CalendarHours open/close window
    //segments that fall entirely outside that window collapse to zero width and are dropped
    private DayColumnDTO toDayColumn(LocalDate day, List<Timeline.Segment> segments) {
        List<SegmentDTO> cells = new ArrayList<>();
        for (Timeline.Segment seg : segments) {
            LocalTime start = seg.start().toLocalTime();
            LocalTime end = seg.end().toLocalTime();
            if (start.isBefore(CalendarHours.OPEN)) start = CalendarHours.OPEN;
            if (end.isAfter(CalendarHours.CLOSE)) end = CalendarHours.CLOSE;
            if (!start.isBefore(end)) continue;
            cells.add(new SegmentDTO(start, end, seg.status().name()));
        }
        return new DayColumnDTO(day, day.getDayOfWeek().name(), cells);
    }

    //assembles the response; assumes monday is actually the week's Monday, so +6 days lands on Sunday
    private WeekCalendarDTO buildWeek(LocalDate monday, List<DayColumnDTO> days) {
        return new WeekCalendarDTO(monday, monday.plusDays(6),
                CalendarHours.OPEN, CalendarHours.CLOSE, days);
    }

    //lower bound of the query window; pairs with weekEnd's upper bound
    private LocalDateTime weekStart(LocalDate monday) {
        return monday.atStartOfDay();
    }

    //cutoff is 23:59:59, not midnight of the next day
    //a booking in the last sub-second of Sunday would be excluded
    // though times are always on the hour/half-hour in practice
    private LocalDateTime weekEnd(LocalDate monday) {
        return monday.plusDays(6).atTime(23, 59, 59);
    }

    //DayOfWeek.getValue() is ISO-numbered (Monday=1..Sunday=7)
    //subtracting (value - 1) days from today always lands on that week's Monday
    private static LocalDate mondayOf(int weekOffset) {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        return thisMonday.plusWeeks(weekOffset);
    }
}
