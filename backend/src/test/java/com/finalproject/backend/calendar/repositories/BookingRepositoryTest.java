package com.finalproject.backend.calendar.repositories;

import com.finalproject.backend.repositories.BookingRepository;
import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRepositoryTest extends RepositoryTestBase {

    @Autowired
    private BookingRepository bookingRepository;

    private static final LocalDate DAY = LocalDate.of(2030, 1, 7);

    @Test
    void findForServiceBetweenIsInclusiveOnBothBoundaries() {
        User provider = newUser();
        User taker = newUser();
        Service service = newService(provider, 1);
        LocalDateTime windowStart = DAY.atStartOfDay();
        LocalDateTime windowEnd = DAY.atTime(23, 59, 59);

        Slot atStart = newSlot(service, windowStart, windowStart.plusHours(1));
        Slot atEnd = newSlot(service, windowEnd, windowEnd.plusHours(1));
        Slot dayBefore = newSlot(service, windowStart.minusSeconds(1), windowStart.plusHours(1));
        Slot dayAfter = newSlot(service, windowEnd.plusSeconds(1), windowEnd.plusHours(2));
        newBooking(taker, atStart, "BOOKED");
        newBooking(taker, atEnd, "BOOKED");
        newBooking(taker, dayBefore, "BOOKED");
        newBooking(taker, dayAfter, "BOOKED");
        em.flush();

        List<Booking> found = bookingRepository
                .findForServiceBetween(service.getId(), windowStart, windowEnd);

        assertThat(found).extracting(b -> b.getSlot().getSlotId())
                .containsExactlyInAnyOrder(atStart.getSlotId(), atEnd.getSlotId());
    }

    @Test
    void findForServiceBetweenIgnoresOtherServices() {
        User provider = newUser();
        User taker = newUser();
        Service mine = newService(provider, 1);
        Service other = newService(provider, 1);
        newBooking(taker, newSlot(other, DAY.atTime(10, 0), DAY.atTime(11, 0)), "BOOKED");
        em.flush();

        assertThat(bookingRepository
                .findForServiceBetween(mine.getId(), DAY.atStartOfDay(), DAY.atTime(23, 59, 59)))
                .isEmpty();
    }

    @Test
    void findForTakerBetweenOnlyReturnsThatTakersBookings() {
        User provider = newUser();
        User me = newUser();
        User someoneElse = newUser();
        Service service = newService(provider, 2);
        Slot slot = newSlot(service, DAY.atTime(10, 0), DAY.atTime(11, 0));
        newBooking(me, slot, "PENDING");
        newBooking(someoneElse, slot, "BOOKED");
        em.flush();

        List<Booking> found = bookingRepository
                .findForTakerBetween(me.getId(), DAY.atStartOfDay(), DAY.atTime(23, 59, 59));

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId().getTakerId()).isEqualTo(me.getId());
    }

    @Test
    void overlappingLookupUsesTrueOverlapNotTouching() {
        User owner = newUser();
        Service service = newService(owner, 1);
        Slot block = newSlot(service, DAY.atTime(10, 0), DAY.atTime(11, 0));
        newBooking(owner, block, "BOOKED");
        em.flush();

        List<Booking> touching = bookingRepository.findForServiceAndTakerOverlapping(
                service.getId(), owner.getId(), DAY.atTime(11, 0), DAY.atTime(12, 0));
        List<Booking> partial = bookingRepository.findForServiceAndTakerOverlapping(
                service.getId(), owner.getId(), DAY.atTime(10, 30), DAY.atTime(11, 30));
        List<Booking> containing = bookingRepository.findForServiceAndTakerOverlapping(
                service.getId(), owner.getId(), DAY.atTime(9, 0), DAY.atTime(12, 0));

        assertThat(touching).isEmpty();
        assertThat(partial).hasSize(1);
        assertThat(containing).hasSize(1);
    }

    @Test
    void overlappingLookupIgnoresOtherTakers() {
        User owner = newUser();
        User customer = newUser();
        Service service = newService(owner, 1);
        Slot slot = newSlot(service, DAY.atTime(10, 0), DAY.atTime(11, 0));
        newBooking(customer, slot, "BOOKED");
        em.flush();

        assertThat(bookingRepository.findForServiceAndTakerOverlapping(
                service.getId(), owner.getId(), DAY.atTime(9, 0), DAY.atTime(12, 0)))
                .isEmpty();
    }

    @Test
    void countBySlotIdCountsAllBookingsOnTheSlot() {
        User provider = newUser();
        User takerA = newUser();
        User takerB = newUser();
        Service service = newService(provider, 2);
        Slot shared = newSlot(service, DAY.atTime(10, 0), DAY.atTime(11, 0));
        Slot empty = newSlot(service, DAY.atTime(12, 0), DAY.atTime(13, 0));
        newBooking(takerA, shared, "PENDING");
        newBooking(takerB, shared, "BOOKED");
        em.flush();

        assertThat(bookingRepository.countBySlot_SlotId(shared.getSlotId())).isEqualTo(2);
        assertThat(bookingRepository.countBySlot_SlotId(empty.getSlotId())).isZero();
    }
}
