package com.finalproject.backend.calendar.services;

import com.finalproject.backend.calendar.model.CalendarHours;
import com.finalproject.backend.calendar.model.SlotStatus;
import com.finalproject.backend.calendar.model.Timeline;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class BookingService {

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

    //looks up the email via slot -> service -> provider -> user chain
    //takes a slotId, not a serviceId, despite returning the service owner's email
    @Transactional(readOnly = true)
    public Optional<String> getServiceOwnerEmail(Integer slotId) {
        return serviceRepository.findOwnerEmailBySlotId(slotId);
    }

    //locks the service row (FOR UPDATE) so concurrent requests for the same service serialize
    //counts pending bookings toward capacity, not just confirmed ones
    //returns empty (not a throw) specifically when the window is already at capacity; invalid input still throws
    @Transactional
    public Optional<BookingCreated> requestBooking(Integer serviceId, LocalDate date,
                                                   LocalTime start, LocalTime end, Integer takerId) {
        validateRange(serviceId, date, start, end);

        int capacity = serviceRepository.lockAndGetCapacity(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found."));

        LocalDateTime startDateTime = date.atTime(start);
        LocalDateTime endDateTime = date.atTime(end);

        List<Timeline.Busy> busy = busyForDay(serviceId, date);
        if (Timeline.maxConcurrency(busy, startDateTime, endDateTime) >= capacity) {
            return Optional.empty();
        }

        Slot slot = new Slot();
        slot.setServiceId(entityManager.getReference(com.finalproject.backend.entities.Service.class, serviceId));
        slot.setStartTime(startDateTime);
        slot.setEndTime(endDateTime);
        slot = slotsRepository.save(slot);

        Booking booking = new Booking();
        booking.setId(new BookingID(takerId, slot.getSlotId()));
        booking.setUser(entityManager.getReference(User.class, takerId));
        booking.setSlot(slot);
        booking.setStatus(SlotStatus.PENDING.name());
        bookingRepository.save(booking);

        return Optional.of(new BookingCreated(slot.getSlotId(), startDateTime, endDateTime));
    }

    //returns empty both when the booking doesn't exist and when it exists but isn't PENDING
    //caller can't distinguish "not found" from "wrong state" from this alone
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

    //same empty-for-not-found-or-wrong-state behavior as confirmBooking
    //deletes the booking row outright, then also deletes the slot if it has no other bookings left
    //slots are created per-request, so an orphaned one would otherwise linger forever
    @Transactional
    public Optional<BookingActionResult> rejectBooking(Integer slotId, Integer takerId) {
        Optional<Booking> opt = bookingRepository.findById(new BookingID(takerId, slotId));
        if (opt.isEmpty()) return Optional.empty();

        Booking booking = opt.get();
        if (!SlotStatus.PENDING.name().equalsIgnoreCase(booking.getStatus())) return Optional.empty();

        BookingActionResult result = toActionResult(booking);
        bookingRepository.delete(booking);

        if (bookingRepository.countBySlot_SlotId(slotId) == 0) {
            slotsRepository.deleteById(slotId);
        }
        return Optional.of(result);
    }

    //owner blocks a range by creating capacity slots, each booked by the owner
    //fills concurrency to capacity so requestBooking rejects everyone else
    //returns false when the caller isn't the service's provider
    @Transactional
    public boolean blockTime(Integer serviceId, LocalDate date,
                             LocalTime start, LocalTime end, Integer ownerId) {
        validateRange(serviceId, date, start, end);
        if (!isOwner(serviceId, ownerId)) {
            return false;
        }

        int capacity = serviceRepository.lockAndGetCapacity(serviceId).orElse(1);
        LocalDateTime startDateTime = date.atTime(start);
        LocalDateTime endDateTime = date.atTime(end);

        for (int i = 0; i < capacity; i++) {
            Slot slot = new Slot();
            slot.setServiceId(entityManager.getReference(com.finalproject.backend.entities.Service.class, serviceId));
            slot.setStartTime(startDateTime);
            slot.setEndTime(endDateTime);
            slot = slotsRepository.save(slot);

            Booking booking = new Booking();
            booking.setId(new BookingID(ownerId, slot.getSlotId()));
            booking.setUser(entityManager.getReference(User.class, ownerId));
            booking.setSlot(slot);
            booking.setStatus(SlotStatus.BOOKED.name());
            bookingRepository.save(booking);
        }
        return true;
    }

    //removes only bookings whose taker is the owner, so customer bookings survive
    //matches by overlap, not exact range, since displayed segments get sliced at booking boundaries
    //deletes each slot left with zero bookings, same cleanup as rejectBooking
    @Transactional
    public boolean unblockTime(Integer serviceId, LocalDate date,
                               LocalTime start, LocalTime end, Integer ownerId) {
        validateRange(serviceId, date, start, end);
        if (!isOwner(serviceId, ownerId)) {
            return false;
        }

        List<Booking> blocks = bookingRepository.findForServiceAndTakerOverlapping(
                serviceId, ownerId, date.atTime(start), date.atTime(end));
        for (Booking booking : blocks) {
            Integer slotId = booking.getSlot().getSlotId();
            bookingRepository.delete(booking);
            if (bookingRepository.countBySlot_SlotId(slotId) == 0) {
                slotsRepository.deleteById(slotId);
            }
        }
        return true;
    }

    //throws for a missing service (400) but returns false for a wrong user (403)
    //so the controller can tell the two cases apart
    private boolean isOwner(Integer serviceId, Integer userId) {
        Integer providerId = serviceRepository.findProviderIdByServiceId(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found."));
        return providerId.equals(userId);
    }

    //shared input checks for requestBooking, blockTime and unblockTime
    //throws IllegalArgumentException, which controllers translate to 400
    private void validateRange(Integer serviceId, LocalDate date, LocalTime start, LocalTime end) {
        if (serviceId == null || date == null || start == null || end == null) {
            throw new IllegalArgumentException("Missing booking details.");
        }
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        if (start.isBefore(CalendarHours.OPEN) || end.isAfter(CalendarHours.CLOSE)) {
            throw new IllegalArgumentException("Requested time is outside working hours.");
        }
    }

    //builds the busy list requestBooking checks capacity against, for a single day
    //skips any booking whose slot data is incomplete instead of failing
    private List<Timeline.Busy> busyForDay(Integer serviceId, LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(23, 59, 59);

        List<Timeline.Busy> busy = new ArrayList<>();
        for (Booking booking : bookingRepository
                .findForServiceBetween(serviceId, dayStart, dayEnd)) {
            Slot slot = booking.getSlot();
            if (slot == null || slot.getStartTime() == null || slot.getEndTime() == null) continue;
            boolean pending = SlotStatus.fromString(booking.getStatus()) == SlotStatus.PENDING;
            busy.add(new Timeline.Busy(slot.getStartTime(), slot.getEndTime(), pending));
        }
        return busy;
    }

    //packages the taker's name/email and slot time for the confirm/reject email notification
    private BookingActionResult toActionResult(Booking booking) {
        User user = booking.getUser();
        String name = user.getFirstName() + " " + user.getLastName();
        return new BookingActionResult(name, user.getEmail(), booking.getSlot().getStartTime());
    }

    //return value of a successful requestBooking
    public record BookingCreated(Integer slotId, LocalDateTime start, LocalDateTime end) {}

    //return value of confirmBooking/rejectBooking
    public record BookingActionResult(String clientName, String clientEmail, LocalDateTime slotDateTime) {}
}
