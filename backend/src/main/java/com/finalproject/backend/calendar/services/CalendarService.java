package com.finalproject.backend.calendar.services;

import com.finalproject.backend.calendar.dtos.DayDTO;
import com.finalproject.backend.calendar.dtos.SlotCellDTO;
import com.finalproject.backend.calendar.dtos.WeeklyCalendarDTO;
import com.finalproject.backend.calendar.model.SlotStatus;
import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.repositories.BookingRepository;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.SlotsRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class CalendarService {

    private static final int[] SLOT_HOURS = {9, 10, 11, 12, 13, 14, 15, 16, 17};

    private final ServiceRepository serviceRepository;
    private final SlotsRepository slotsRepository;
    private final BookingRepository bookingRepository;

    public CalendarService(ServiceRepository serviceRepository,
                           SlotsRepository slotsRepository,
                           BookingRepository bookingRepository) {
        this.serviceRepository = serviceRepository;
        this.slotsRepository = slotsRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public WeeklyCalendarDTO getWeeklyCalendarForUser(Integer userId, int weekOffset) {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekMonday = thisMonday.plusWeeks(weekOffset);
        LocalDate weekSunday = weekMonday.plusDays(6);

        Map<LocalDateTime, SlotStatus> statusByTime = new HashMap<>();

        LocalDateTime weekStart = weekMonday.atStartOfDay();
        LocalDateTime weekEnd = weekSunday.atTime(23, 59, 59);

        for (Service service : serviceRepository.findByProviderId_Id(userId)) {
            int capacity = service.getMaxCapacity() != null ? service.getMaxCapacity() : 1;

            List<Slot> slots = slotsRepository.findByServiceId_IdAndStartTimeBetweenOrderByStartTime(
                    service.getId(), weekStart, weekEnd);
            if (slots.isEmpty()) continue;

            List<Integer> slotIds = slots.stream().map(Slot::getSlotId).collect(Collectors.toList());
            Map<Integer, Integer> occupiedBySlot = new HashMap<>();
            Map<Integer, Boolean> pendingBySlot = new HashMap<>();
            for (Booking booking : bookingRepository.findBySlot_SlotIdIn(slotIds)) {
                Integer sid = booking.getId().getSlotId();
                occupiedBySlot.merge(sid, 1, Integer::sum);
                if (SlotStatus.fromString(booking.getStatus()) == SlotStatus.PENDING) {
                    pendingBySlot.put(sid, true);
                }
            }

            for (Slot slot : slots) {
                int occupied = occupiedBySlot.getOrDefault(slot.getSlotId(), 0);
                boolean anyPending = pendingBySlot.getOrDefault(slot.getSlotId(), false);
                SlotStatus status = SlotStatus.forCapacity(occupied, anyPending, capacity);
                statusByTime.merge(slot.getStartTime(), status, SlotStatus::higher);
            }
        }

        List<DayDTO> days = new ArrayList<>();
        for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
            LocalDate day = weekMonday.plusDays(dayOffset);
            List<SlotCellDTO> cells = new ArrayList<>();
            for (int hour : SLOT_HOURS) {
                LocalDateTime dateTime = day.atTime(hour, 0);
                SlotStatus status = statusByTime.getOrDefault(dateTime, SlotStatus.FREE);
                cells.add(new SlotCellDTO(LocalTime.of(hour, 0), status.name()));
            }
            days.add(new DayDTO(day, day.getDayOfWeek().name(), cells));
        }

        return new WeeklyCalendarDTO(userId, weekMonday, weekSunday, days);
    }
}
