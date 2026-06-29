package com.finalproject.backend.booking;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingSlotRepository slotRepository;

    public BookingService(BookingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    /** Hours available for booking each day (9 AM – 5 PM). */
    private static final int[] SLOT_HOURS = {9, 10, 11, 12, 13, 14, 15, 16, 17};

    /**
     * Returns slots for the week at the given offset from the current week.
     * weekOffset = 0  → this week
     * weekOffset = 1  → next week
     * weekOffset = -1 → last week
     *
     * If no slots exist for the requested week they are auto-generated so the
     * calendar is never empty regardless of when the DB was first seeded.
     */
    @Transactional
    public List<BookingSlotDTO> getAllSlotsForWeek(Integer serviceId, int weekOffset) {
        LocalDate today      = LocalDate.now();
        LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekMonday = thisMonday.plusWeeks(weekOffset);

        LocalDateTime weekStart = weekMonday.atStartOfDay();
        LocalDateTime weekEnd   = weekMonday.plusDays(6).atTime(23, 59, 59);

        List<BookingSlot> slots = slotRepository
                .findByServiceIdAndSlotDateTimeBetweenOrderBySlotDateTime(serviceId, weekStart, weekEnd);

        if (slots.isEmpty()) {
            slots = generateSlotsForWeek(serviceId, weekMonday);
        }

        return slots.stream()
                .map(s -> new BookingSlotDTO(s.getId(), s.getSlotDateTime(), s.getStatus().name()))
                .collect(Collectors.toList());
    }

    private List<BookingSlot> generateSlotsForWeek(Integer serviceId, LocalDate monday) {
        List<BookingSlot> newSlots = new ArrayList<>();
        for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
            LocalDate day = monday.plusDays(dayOffset);
            for (int hour : SLOT_HOURS) {
                BookingSlot slot = new BookingSlot();
                slot.setServiceId(serviceId);
                slot.setSlotDateTime(day.atTime(hour, 0));
                slot.setStatus(SlotStatus.FREE);
                newSlots.add(slot);
            }
        }
        return slotRepository.saveAll(newSlots);
    }

    /** FREE → PENDING */
    @Transactional
    public Optional<BookingSlotDTO> requestBooking(BookingRequestDTO dto) {
        Optional<BookingSlot> opt = slotRepository.findById(dto.getSlotId());
        if (opt.isEmpty()) return Optional.empty();

        BookingSlot slot = opt.get();
        if (slot.getStatus() != SlotStatus.FREE) return Optional.empty();

        slot.setStatus(SlotStatus.PENDING);
        slot.setClientName(dto.getClientName());
        slot.setClientEmail(dto.getClientEmail());
        BookingSlot saved = slotRepository.save(slot);

        return Optional.of(new BookingSlotDTO(
                saved.getId(), saved.getSlotDateTime(), saved.getStatus().name()));
    }

    /** PENDING → BOOKED */
    @Transactional
    public Optional<BookingSlot> confirmBooking(Long slotId) {
        Optional<BookingSlot> opt = slotRepository.findById(slotId);
        if (opt.isEmpty()) return Optional.empty();

        BookingSlot slot = opt.get();
        if (slot.getStatus() != SlotStatus.PENDING) return Optional.empty();

        slot.setStatus(SlotStatus.BOOKED);
        return Optional.of(slotRepository.save(slot));
    }

    /** PENDING → FREE */
    @Transactional
    public Optional<BookingSlot> rejectBooking(Long slotId) {
        Optional<BookingSlot> opt = slotRepository.findById(slotId);
        if (opt.isEmpty()) return Optional.empty();

        BookingSlot slot = opt.get();
        if (slot.getStatus() != SlotStatus.PENDING) return Optional.empty();

        slot.setStatus(SlotStatus.FREE);
        slot.setClientName(null);
        slot.setClientEmail(null);
        return Optional.of(slotRepository.save(slot));
    }
}
