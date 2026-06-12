package com.finalproject.backend.booking;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Service layer for calendar slot operations.
 * MOCK IMPLEMENTATION – uses an in-memory ConcurrentHashMap.
 * Replace map calls with BookingSlotRepository when DB is ready.
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
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);

        SlotStatus[] pattern = {
            SlotStatus.FREE, SlotStatus.FREE,    SlotStatus.FREE,
            SlotStatus.FREE, SlotStatus.BOOKED,  SlotStatus.FREE,
            SlotStatus.FREE, SlotStatus.PENDING, SlotStatus.FREE
        };
        int pIdx = 0;

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

    // ── Public methods ────────────────────────────────────────────────────────

    /** Returns all slots for the current week sorted by date/time. */
    public List<BookingSlotDTO> getAllSlotsForCurrentWeek() {
        return mockStore.values().stream()
            .sorted(Comparator.comparing(BookingSlot::getSlotDateTime))
            .map(s -> new BookingSlotDTO(s.getId(), s.getSlotDateTime(), s.getStatus().name()))
            .collect(Collectors.toList());
    }

    /** FREE → PENDING. Returns empty if slot not found or not FREE. */
    public Optional<BookingSlotDTO> requestBooking(BookingRequestDTO dto) {
        BookingSlot slot = mockStore.get(dto.getSlotId());
        if (slot == null || slot.getStatus() != SlotStatus.FREE) {
            return Optional.empty();
        }
        slot.setStatus(SlotStatus.PENDING);
        slot.setClientName(dto.getClientName());
        slot.setClientEmail(dto.getClientEmail());
        mockStore.put(slot.getId(), slot);
        return Optional.of(new BookingSlotDTO(slot.getId(), slot.getSlotDateTime(), slot.getStatus().name()));
    }

    /** PENDING → BOOKED. Returns empty if slot not found or not PENDING. */
    public Optional<BookingSlot> confirmBooking(Long slotId) {
        BookingSlot slot = mockStore.get(slotId);
        if (slot == null || slot.getStatus() != SlotStatus.PENDING) {
            return Optional.empty();
        }
        slot.setStatus(SlotStatus.BOOKED);
        mockStore.put(slot.getId(), slot);
        return Optional.of(slot);
    }

    /** PENDING → FREE (slot freed again). Returns empty if not found or not PENDING. */
    public Optional<BookingSlot> rejectBooking(Long slotId) {
        BookingSlot slot = mockStore.get(slotId);
        if (slot == null || slot.getStatus() != SlotStatus.PENDING) {
            return Optional.empty();
        }
        slot.setStatus(SlotStatus.FREE);
        slot.setClientName(null);
        slot.setClientEmail(null);
        mockStore.put(slot.getId(), slot);
        return Optional.of(slot);
    }
}
