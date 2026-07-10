package com.finalproject.backend.calendar.services;

import com.finalproject.backend.calendar.dtos.DayColumnDTO;
import com.finalproject.backend.calendar.dtos.WeekCalendarDTO;
import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.repositories.BookingRepository;
import com.finalproject.backend.repositories.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private CalendarService calendarService;

    private static LocalDate currentMonday() {
        LocalDate today = LocalDate.now();
        return today.minusDays(today.getDayOfWeek().getValue() - 1);
    }

    private static Booking booking(LocalDateTime start, LocalDateTime end, String status) {
        Slot slot = new Slot();
        slot.setStartTime(start);
        slot.setEndTime(end);
        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setStatus(status);
        return booking;
    }

    @Test
    void unknownServiceYieldsEmptySoControllerCan404() {
        when(serviceRepository.findMaxCapacityByServiceId(99)).thenReturn(Optional.empty());

        assertThat(calendarService.getServiceWeek(99, 0)).isEmpty();
    }

    @Test
    void serviceWithNoBookingsStillReturnsAFullEmptyWeek() {
        when(serviceRepository.findMaxCapacityByServiceId(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of());

        WeekCalendarDTO week = calendarService.getServiceWeek(1, 0).orElseThrow();

        LocalDate monday = currentMonday();
        assertThat(week.getWeekStart()).isEqualTo(monday);
        assertThat(week.getWeekEnd()).isEqualTo(monday.plusDays(6));
        assertThat(week.getDays()).hasSize(7);
        assertThat(week.getDays().get(0).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY.name());
        assertThat(week.getDays().get(6).getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY.name());
        assertThat(week.getDays()).allSatisfy(day -> assertThat(day.getSegments()).isEmpty());
    }

    @Test
    void weekOffsetShiftsTheReturnedWeek() {
        when(serviceRepository.findMaxCapacityByServiceId(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of());

        WeekCalendarDTO week = calendarService.getServiceWeek(1, 2).orElseThrow();

        assertThat(week.getWeekStart()).isEqualTo(currentMonday().plusWeeks(2));
    }

    @Test
    void pendingBookingShowsAsPendingSegmentOnItsDay() {
        LocalDate monday = currentMonday();
        when(serviceRepository.findMaxCapacityByServiceId(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(
                booking(monday.atTime(10, 0), monday.atTime(11, 30), "PENDING")));

        WeekCalendarDTO week = calendarService.getServiceWeek(1, 0).orElseThrow();

        DayColumnDTO mondayColumn = week.getDays().get(0);
        assertThat(mondayColumn.getSegments()).hasSize(1);
        assertThat(mondayColumn.getSegments().get(0).getStart()).isEqualTo(LocalTime.of(10, 0));
        assertThat(mondayColumn.getSegments().get(0).getEnd()).isEqualTo(LocalTime.of(11, 30));
        assertThat(mondayColumn.getSegments().get(0).getStatus()).isEqualTo("PENDING");
        assertThat(week.getDays().subList(1, 7)).allSatisfy(day -> assertThat(day.getSegments()).isEmpty());
    }

    @Test
    void unrecognizedStatusCountsAsBooked() {
        LocalDate monday = currentMonday();
        when(serviceRepository.findMaxCapacityByServiceId(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(
                booking(monday.atTime(9, 0), monday.atTime(10, 0), "CONFIRMED")));

        WeekCalendarDTO week = calendarService.getServiceWeek(1, 0).orElseThrow();

        assertThat(week.getDays().get(0).getSegments().get(0).getStatus()).isEqualTo("BOOKED");
    }

    @Test
    void bookingBelowCapacityLeavesTheDayFree() {
        LocalDate monday = currentMonday();
        when(serviceRepository.findMaxCapacityByServiceId(1)).thenReturn(Optional.of(2));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(
                booking(monday.atTime(10, 0), monday.atTime(11, 0), "BOOKED")));

        WeekCalendarDTO week = calendarService.getServiceWeek(1, 0).orElseThrow();

        assertThat(week.getDays()).allSatisfy(day -> assertThat(day.getSegments()).isEmpty());
    }

    @Test
    void bookingWithIncompleteSlotDataIsSkippedNotFatal() {
        Booking broken = new Booking();
        broken.setStatus("BOOKED");
        when(serviceRepository.findMaxCapacityByServiceId(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(broken));

        WeekCalendarDTO week = calendarService.getServiceWeek(1, 0).orElseThrow();

        assertThat(week.getDays()).allSatisfy(day -> assertThat(day.getSegments()).isEmpty());
    }

    @Test
    void userWeekOverlaysProviderAndOwnBookingsPickingTheSevereStatus() {
        LocalDate monday = currentMonday();
        when(serviceRepository.findServiceCapacitiesByProvider(7))
                .thenReturn(List.<Object[]>of(new Object[]{5, 1}));
        when(bookingRepository.findForServiceBetween(eq(5), any(), any())).thenReturn(List.of(
                booking(monday.atTime(10, 0), monday.atTime(11, 0), "PENDING")));
        when(bookingRepository.findForTakerBetween(eq(7), any(), any())).thenReturn(List.of(
                booking(monday.atTime(10, 30), monday.atTime(11, 30), "BOOKED")));

        WeekCalendarDTO week = calendarService.getUserWeek(7, 0);

        DayColumnDTO mondayColumn = week.getDays().get(0);
        assertThat(mondayColumn.getSegments()).hasSize(2);
        assertThat(mondayColumn.getSegments().get(0).getStart()).isEqualTo(LocalTime.of(10, 0));
        assertThat(mondayColumn.getSegments().get(0).getEnd()).isEqualTo(LocalTime.of(10, 30));
        assertThat(mondayColumn.getSegments().get(0).getStatus()).isEqualTo("PENDING");
        assertThat(mondayColumn.getSegments().get(1).getStart()).isEqualTo(LocalTime.of(10, 30));
        assertThat(mondayColumn.getSegments().get(1).getEnd()).isEqualTo(LocalTime.of(11, 30));
        assertThat(mondayColumn.getSegments().get(1).getStatus()).isEqualTo("BOOKED");
    }

    @Test
    void midnightCrossingBookingIsDroppedFromTheDayColumn() {
        LocalDate monday = currentMonday();
        when(serviceRepository.findMaxCapacityByServiceId(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(
                booking(monday.atTime(23, 0), monday.plusDays(1).atTime(1, 0), "BOOKED")));

        WeekCalendarDTO week = calendarService.getServiceWeek(1, 0).orElseThrow();

        assertThat(week.getDays()).allSatisfy(day -> assertThat(day.getSegments()).isEmpty());
    }

    @Test
    void segmentEndIsClampedToClosingTime() {
        LocalDate monday = currentMonday();
        when(serviceRepository.findMaxCapacityByServiceId(1)).thenReturn(Optional.of(1));
        when(bookingRepository.findForServiceBetween(eq(1), any(), any())).thenReturn(List.of(
                booking(monday.atTime(23, 0), monday.atTime(23, 59, 30), "BOOKED")));

        WeekCalendarDTO week = calendarService.getServiceWeek(1, 0).orElseThrow();

        assertThat(week.getDays().get(0).getSegments().get(0).getEnd())
                .isEqualTo(LocalTime.of(23, 59));
    }

    @Test
    void userWithNoServicesAndNoBookingsGetsAnEmptyWeek() {
        when(serviceRepository.findServiceCapacitiesByProvider(7)).thenReturn(List.of());
        when(bookingRepository.findForTakerBetween(eq(7), any(), any())).thenReturn(List.of());

        WeekCalendarDTO week = calendarService.getUserWeek(7, 0);

        assertThat(week.getDays()).hasSize(7);
        assertThat(week.getDays()).allSatisfy(day -> assertThat(day.getSegments()).isEmpty());
    }
}
