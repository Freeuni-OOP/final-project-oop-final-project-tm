package com.finalproject.backend.calendar.services;

import com.finalproject.backend.calendar.model.SlotStatus;
import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.BookingID;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.BookingRepository;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.SlotsRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private SlotsRepository slotsRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BookingService bookingService;

    private static final LocalDate DATE = LocalDate.of(2026, 7, 6);

    @BeforeEach
    void injectEntityManager() {
        org.springframework.test.util.ReflectionTestUtils
                .setField(bookingService, "entityManager", entityManager);
    }

    private void stubSlotSaveAssignsIds() {
        AtomicInteger nextId = new AtomicInteger(100);
        when(slotsRepository.save(any(Slot.class))).thenAnswer(invocation -> {
            Slot slot = invocation.getArgument(0);
            slot.setSlotId(nextId.getAndIncrement());
            return slot;
        });
    }

    private static Booking existingBooking(Integer takerId, Integer slotId,
                                           LocalDateTime start, LocalDateTime end, String status) {
        Slot slot = new Slot();
        slot.setSlotId(slotId);
        slot.setStartTime(start);
        slot.setEndTime(end);

        User user = new User();
        user.setId(takerId);
        user.setFirstName("Ana");
        user.setLastName("Bee");
        user.setEmail("ana@example.com");

        Booking booking = new Booking();
        booking.setId(new BookingID(takerId, slotId));
        booking.setUser(user);
        booking.setSlot(slot);
        booking.setStatus(status);
        return booking;
    }

    @Test
    void requestBookingRejectsMissingDetails() {
        assertThatThrownBy(() -> bookingService.requestBooking(1, null, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing booking details.");
    }

    @Test
    void requestBookingRejectsStartNotBeforeEnd() {
        assertThatThrownBy(() -> bookingService.requestBooking(1, DATE, LocalTime.of(11, 0), LocalTime.of(11, 0), 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Start time must be before end time.");
    }

    @Test
    void requestBookingRejectsUnknownService() {
        when(serviceRepository.lockAndGetCapacity(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.requestBooking(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Service not found.");
    }

    @Test
    void requestBookingReturnsEmptyWhenWindowIsAtCapacity() {
        when(serviceRepository.lockAndGetCapacity(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(
                existingBooking(2, 50, DATE.atTime(10, 0), DATE.atTime(12, 0), "BOOKED")));

        Optional<BookingService.BookingCreated> result =
                bookingService.requestBooking(1, DATE, LocalTime.of(10, 30), LocalTime.of(11, 30), 7);

        assertThat(result).isEmpty();
        verify(slotsRepository, never()).save(any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void requestBookingCreatesPendingBookingWhenCapacityAllows() {
        stubSlotSaveAssignsIds();
        when(serviceRepository.lockAndGetCapacity(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of());

        Optional<BookingService.BookingCreated> result =
                bookingService.requestBooking(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7);

        assertThat(result).isPresent();
        assertThat(result.get().slotId()).isEqualTo(100);
        assertThat(result.get().start()).isEqualTo(DATE.atTime(10, 0));
        assertThat(result.get().end()).isEqualTo(DATE.atTime(11, 0));

        ArgumentCaptor<Booking> saved = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(saved.capture());
        assertThat(saved.getValue().getStatus()).isEqualTo(SlotStatus.PENDING.name());
        assertThat(saved.getValue().getId().getTakerId()).isEqualTo(7);
        assertThat(saved.getValue().getId().getSlotId()).isEqualTo(100);
    }

    @Test
    void requestBookingSucceedsNextToAnAdjacentBooking() {
        stubSlotSaveAssignsIds();
        when(serviceRepository.lockAndGetCapacity(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(
                existingBooking(2, 50, DATE.atTime(9, 0), DATE.atTime(10, 0), "BOOKED")));

        Optional<BookingService.BookingCreated> result =
                bookingService.requestBooking(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7);

        assertThat(result).isPresent();
    }

    @Test
    void requestBookingRejectsTimesPastClosing() {
        assertThatThrownBy(() -> bookingService.requestBooking(1, DATE, LocalTime.of(23, 0), LocalTime.of(23, 59, 30), 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Requested time is outside working hours.");
    }

    @Test
    void requestBookingIgnoresExistingBookingsWithBrokenSlotData() {
        stubSlotSaveAssignsIds();
        Booking broken = new Booking();
        broken.setStatus("BOOKED");
        when(serviceRepository.lockAndGetCapacity(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(broken));

        Optional<BookingService.BookingCreated> result =
                bookingService.requestBooking(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7);

        assertThat(result).isPresent();
    }

    @Test
    void blockTimeOnUnknownServiceThrowsInsteadOfReturningFalse() {
        when(serviceRepository.findProviderIdByServiceId(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.blockTime(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Service not found.");
    }

    @Test
    void confirmBookingReturnsEmptyWhenBookingDoesNotExist() {
        when(bookingRepository.findById(new BookingID(7, 50))).thenReturn(Optional.empty());

        assertThat(bookingService.confirmBooking(50, 7)).isEmpty();
    }

    @Test
    void confirmBookingReturnsEmptyWhenNotPending() {
        when(bookingRepository.findById(new BookingID(7, 50))).thenReturn(Optional.of(
                existingBooking(7, 50, DATE.atTime(10, 0), DATE.atTime(11, 0), "BOOKED")));

        assertThat(bookingService.confirmBooking(50, 7)).isEmpty();
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void confirmBookingMarksPendingAsBookedAndReturnsClientDetails() {
        Booking booking = existingBooking(7, 50, DATE.atTime(10, 0), DATE.atTime(11, 0), "PENDING");
        when(bookingRepository.findById(new BookingID(7, 50))).thenReturn(Optional.of(booking));

        Optional<BookingService.BookingActionResult> result = bookingService.confirmBooking(50, 7);

        assertThat(result).isPresent();
        assertThat(result.get().clientName()).isEqualTo("Ana Bee");
        assertThat(result.get().clientEmail()).isEqualTo("ana@example.com");
        assertThat(result.get().slotDateTime()).isEqualTo(DATE.atTime(10, 0));
        assertThat(booking.getStatus()).isEqualTo(SlotStatus.BOOKED.name());
        verify(bookingRepository).save(booking);
    }

    @Test
    void rejectBookingDeletesBookingAndOrphanedSlot() {
        Booking booking = existingBooking(7, 50, DATE.atTime(10, 0), DATE.atTime(11, 0), "PENDING");
        when(bookingRepository.findById(new BookingID(7, 50))).thenReturn(Optional.of(booking));
        when(bookingRepository.countBySlot_SlotId(50)).thenReturn(0L);

        Optional<BookingService.BookingActionResult> result = bookingService.rejectBooking(50, 7);

        assertThat(result).isPresent();
        verify(bookingRepository).delete(booking);
        verify(slotsRepository).deleteById(50);
    }

    @Test
    void rejectBookingKeepsSlotStillUsedByOtherBookings() {
        Booking booking = existingBooking(7, 50, DATE.atTime(10, 0), DATE.atTime(11, 0), "PENDING");
        when(bookingRepository.findById(new BookingID(7, 50))).thenReturn(Optional.of(booking));
        when(bookingRepository.countBySlot_SlotId(50)).thenReturn(1L);

        assertThat(bookingService.rejectBooking(50, 7)).isPresent();
        verify(slotsRepository, never()).deleteById(any());
    }

    @Test
    void rejectBookingReturnsEmptyWhenNotPending() {
        when(bookingRepository.findById(new BookingID(7, 50))).thenReturn(Optional.of(
                existingBooking(7, 50, DATE.atTime(10, 0), DATE.atTime(11, 0), "BOOKED")));

        assertThat(bookingService.rejectBooking(50, 7)).isEmpty();
        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void blockTimeRefusesNonOwner() {
        when(serviceRepository.findProviderIdByServiceId(1)).thenReturn(Optional.of(3));

        boolean blocked = bookingService.blockTime(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7);

        assertThat(blocked).isFalse();
        verify(slotsRepository, never()).save(any());
    }

    @Test
    void blockTimeFillsCapacityWithOwnerBookedSlots() {
        stubSlotSaveAssignsIds();
        when(serviceRepository.findProviderIdByServiceId(1)).thenReturn(Optional.of(7));
        when(serviceRepository.lockAndGetCapacity(1)).thenReturn(Optional.of(2));

        boolean blocked = bookingService.blockTime(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7);

        assertThat(blocked).isTrue();
        ArgumentCaptor<Booking> saved = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository, org.mockito.Mockito.times(2)).save(saved.capture());
        assertThat(saved.getAllValues()).allSatisfy(booking -> {
            assertThat(booking.getStatus()).isEqualTo(SlotStatus.BOOKED.name());
            assertThat(booking.getId().getTakerId()).isEqualTo(7);
        });
    }

    @Test
    void unblockTimeRefusesNonOwner() {
        when(serviceRepository.findProviderIdByServiceId(1)).thenReturn(Optional.of(3));

        boolean unblocked = bookingService.unblockTime(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7);

        assertThat(unblocked).isFalse();
        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void unblockTimeDeletesOwnerBlocksAndOrphanedSlots() {
        Booking block = existingBooking(7, 60, DATE.atTime(10, 0), DATE.atTime(11, 0), "BOOKED");
        when(serviceRepository.findProviderIdByServiceId(1)).thenReturn(Optional.of(7));
        when(bookingRepository.findForServiceAndTakerOverlapping(eq(1), eq(7), any(), any()))
                .thenReturn(List.of(block));
        when(bookingRepository.countBySlot_SlotId(60)).thenReturn(0L);

        boolean unblocked = bookingService.unblockTime(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7);

        assertThat(unblocked).isTrue();
        verify(bookingRepository).delete(block);
        verify(slotsRepository).deleteById(60);
    }
}
