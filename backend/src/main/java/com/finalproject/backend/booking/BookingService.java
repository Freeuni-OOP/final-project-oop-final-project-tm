package com.finalproject.backend.booking;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Mock service – uses an in-memory store.
 * Seeds slots for 8 weeks (2 past + current + 5 future) so the user
 * can navigate the calendar freely.
 */
@Service
public class BookingService {

    private final Map<Long, BookingSlot> mockStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public BookingService() {
        seedMockData();
    }

    // ── Seeder ────────────────────────────────────────────────────────────────

    private void seedMockData() {
        LocalDate today  = LocalDate.now();
        LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);

        SlotStatus[] pattern = {
            SlotStatus.FREE, SlotStatus.FREE,    SlotStatus.FREE,
            SlotStatus.FREE, SlotStatus.BOOKED,  SlotStatus.FREE,
            SlotStatus.FREE, SlotStatus.PENDING, SlotStatus.FREE
        };
        int pIdx = 0;

        // Seed from 2 weeks ago up to 5 weeks ahead (8 weeks total)
        for (int weekOffset = -2; weekOffset <= 5; weekOffset++) {
            LocalDate monday = thisMonday.plusWeeks(weekOffset);
            for (int day = 0; day < 7; day++) {
                for (int hour = 9; hour <= 17; hour++) {
                    long id = idCounter.getAndIncrement();
                    mockStore.put(id, new BookingSlot(
                        id,
                        monday.plusDays(day).atTime(hour, 0),
                        pattern[pIdx++ % pattern.length]
                    ));
                }
            }
        }
    }

    // ── Public methods ────────────────────────────────────────────────────────

    /**
     * Returns slots for the week at the given offset from the current week.
     * weekOffset = 0  → this week
     * weekOffset = 1  → next week
     * weekOffset = -1 → last week
     */
    public List<BookingSlotDTO> getAllSlotsForWeek(int weekOffset) {
        LocalDate today      = LocalDate.now();
        LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekMonday = thisMonday.plusWeeks(weekOffset);
        LocalDate weekSunday = weekMonday.plusDays(6);

        return mockStore.values().stream()
            .filter(s -> {
                LocalDate d = s.getSlotDateTime().toLocalDate();
                return !d.isBefore(weekMonday) && !d.isAfter(weekSunday);
            })
            .sorted(Comparator.comparing(BookingSlot::getSlotDateTime))
            .map(s -> new BookingSlotDTO(s.getId(), s.getSlotDateTime(), s.getStatus().name()))
            .collect(Collectors.toList());
    }

    /** FREE → PENDING */
    public Optional<BookingSlotDTO> requestBooking(BookingRequestDTO dto) {
        BookingSlot slot = mockStore.get(dto.getSlotId());
        if (slot == null || slot.getStatus() != SlotStatus.FREE) return Optional.empty();
        slot.setStatus(SlotStatus.PENDING);
        slot.setClientName(dto.getClientName());
        slot.setClientEmail(dto.getClientEmail());
        mockStore.put(slot.getId(), slot);
        return Optional.of(new BookingSlotDTO(slot.getId(), slot.getSlotDateTime(), slot.getStatus().name()));
    }

    /** PENDING → BOOKED */
    public Optional<BookingSlot> confirmBooking(Long slotId) {
        BookingSlot slot = mockStore.get(slotId);
        if (slot == null || slot.getStatus() != SlotStatus.PENDING) return Optional.empty();
        slot.setStatus(SlotStatus.BOOKED);
        mockStore.put(slot.getId(), slot);
        return Optional.of(slot);
    }

    /** PENDING → FREE */
    public Optional<BookingSlot> rejectBooking(Long slotId) {
        BookingSlot slot = mockStore.get(slotId);
        if (slot == null || slot.getStatus() != SlotStatus.PENDING) return Optional.empty();
        slot.setStatus(SlotStatus.FREE);
        slot.setClientName(null);
        slot.setClientEmail(null);
        mockStore.put(slot.getId(), slot);
        return Optional.of(slot);
    }
}
