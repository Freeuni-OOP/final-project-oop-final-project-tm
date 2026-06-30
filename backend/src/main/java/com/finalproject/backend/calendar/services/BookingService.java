package com.finalproject.backend.calendar.services;

import com.finalproject.backend.calendar.dtos.BookingSlotDTO;
import com.finalproject.backend.calendar.model.SlotStatus;
import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.BookingID;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.BookingRepository;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.SlotsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BookingService {

    private static final int[] SLOT_HOURS = {9, 10, 11, 12, 13, 14, 15, 16, 17};

    private final SlotsRepository slotsRepository;
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public BookingService(SlotsRepository slotsRepository,
                          BookingRepository bookingRepository,
                          ServiceRepository serviceRepository) {
        this.slotsRepository = slotsRepository;
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional(readOnly = true)
    public Optional<String> getServiceOwnerEmail(Integer slotId) {
        return serviceRepository.findOwnerEmailBySlotId(slotId);
    }

    @Transactional
    public List<BookingSlotDTO> getAllSlotsForWeek(Integer serviceId, int weekOffset) {
        LocalDate weekMonday = mondayOf(weekOffset);
        LocalDateTime weekStart = weekMonday.atStartOfDay();
        LocalDateTime weekEnd = weekMonday.plusDays(6).atTime(23, 59, 59);

        List<Slot> slots = slotsRepository
                .findByServiceId_IdAndStartTimeBetweenOrderByStartTime(serviceId, weekStart, weekEnd);

        if (slots.isEmpty()) {
            slots = generateSlotsForWeek(serviceId, weekMonday);
        }

        int capacity = serviceRepository.findMaxCapacityByServiceId(serviceId).orElse(1);

        Map<Integer, Integer> occupiedBySlot = new HashMap<>();
        Map<Integer, Boolean> pendingBySlot = new HashMap<>();
        List<Integer> slotIds = slots.stream().map(Slot::getSlotId).collect(Collectors.toList());
        if (!slotIds.isEmpty()) {
            for (Booking booking : bookingRepository.findBySlot_SlotIdIn(slotIds)) {
                Integer sid = booking.getId().getSlotId();
                occupiedBySlot.merge(sid, 1, Integer::sum);
                if (SlotStatus.fromString(booking.getStatus()) == SlotStatus.PENDING) {
                    pendingBySlot.put(sid, true);
                }
            }
        }

        return slots.stream()
                .map(s -> {
                    int occupied = occupiedBySlot.getOrDefault(s.getSlotId(), 0);
                    boolean anyPending = pendingBySlot.getOrDefault(s.getSlotId(), false);
                    SlotStatus status = SlotStatus.forCapacity(occupied, anyPending, capacity);
                    return new BookingSlotDTO(s.getSlotId().longValue(), s.getStartTime(), status.name());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<BookingSlotDTO> requestBooking(Integer slotId, Integer takerId) {
        Optional<Slot> slotOpt = slotsRepository.findById(slotId);
        if (slotOpt.isEmpty()) return Optional.empty();

        BookingID id = new BookingID(takerId, slotId);
        if (bookingRepository.existsById(id)) return Optional.empty();

        int capacity = serviceRepository.findMaxCapacityBySlotId(slotId).orElse(1);
        long occupied = bookingRepository.countBySlot_SlotId(slotId);
        if (occupied >= capacity) return Optional.empty();

        Slot slot = slotOpt.get();

        Booking booking = new Booking();
        booking.setId(id);
        booking.setUser(entityManager.getReference(User.class, takerId));
        booking.setSlot(slot);
        booking.setStatus(SlotStatus.PENDING.name());
        bookingRepository.save(booking);

        SlotStatus status = SlotStatus.forCapacity((int) occupied + 1, true, capacity);
        return Optional.of(new BookingSlotDTO(
                slot.getSlotId().longValue(), slot.getStartTime(), status.name()));
    }

    @Transactional
    public Optional<BookingActionResult> confirmBooking(Integer slotId, Integer takerId) {
        Optional<Booking> opt = bookingRepository.findById(new BookingID(takerId, slotId));
        if (opt.isEmpty()) return Optional.empty();

        Booking booking = opt.get();
        if (!SlotStatus.PENDING.name().equalsIgnoreCase(booking.getStatus())) return Optional.empty();

        booking.setStatus(SlotStatus.BOOKED.name());
        bookingRepository.save(booking);
        return Optional.of(toActionResult(booking));
    }

    @Transactional
    public Optional<BookingActionResult> rejectBooking(Integer slotId, Integer takerId) {
        Optional<Booking> opt = bookingRepository.findById(new BookingID(takerId, slotId));
        if (opt.isEmpty()) return Optional.empty();

        Booking booking = opt.get();
        if (!SlotStatus.PENDING.name().equalsIgnoreCase(booking.getStatus())) return Optional.empty();

        BookingActionResult result = toActionResult(booking);
        bookingRepository.delete(booking);
        return Optional.of(result);
    }

    private List<Slot> generateSlotsForWeek(Integer serviceId, LocalDate monday) {
        com.finalproject.backend.entities.Service serviceRef =
                entityManager.getReference(com.finalproject.backend.entities.Service.class, serviceId);

        List<Slot> newSlots = new ArrayList<>();
        for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
            LocalDate day = monday.plusDays(dayOffset);
            for (int hour : SLOT_HOURS) {
                Slot slot = new Slot();
                slot.setServiceId(serviceRef);
                slot.setStartTime(day.atTime(hour, 0));
                slot.setEndTime(day.atTime(hour + 1, 0));
                newSlots.add(slot);
            }
        }
        return slotsRepository.saveAll(newSlots);
    }

    private BookingActionResult toActionResult(Booking booking) {
        User user = booking.getUser();
        String name = user.getFirstName() + " " + user.getLastName();
        return new BookingActionResult(name, user.getEmail(), booking.getSlot().getStartTime());
    }

    private static LocalDate mondayOf(int weekOffset) {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        return thisMonday.plusWeeks(weekOffset);
    }

    public record BookingActionResult(String clientName, String clientEmail, LocalDateTime slotDateTime) {}
}
