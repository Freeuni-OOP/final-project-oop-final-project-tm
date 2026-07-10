package database;


import com.finalproject.backend.BackendApplication;
import com.finalproject.backend.entities.*;
import com.finalproject.backend.repositories.BookingRepository;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.SlotsRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = BackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingRepoTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private SlotsRepository slotRepository;

    private User buildUser(String email) {
        User u = new User();
        u.setFirstName("Taker");
        u.setLastName("Person");
        u.setEmail(email);
        u.setPassHash("213");
        u.setEnabled(true);
        return userRepository.save(u);
    }

    private Slot buildSlot(String providerEmail) {
        User provider = buildUser(providerEmail);

        Service service = new Service();
        service.setProviderId(provider);
        service.setTitle("Yoga Lesson");
        service.setPrice(30.0);
        serviceRepository.save(service);

        Slot slot = new Slot();
        slot.setServiceId(service);
        return slotRepository.save(slot);
    }

    @Test
    void savesAndGetBooking() {
        User taker = buildUser("taker1@freeuni.com");
        Slot slot = buildSlot("provider1@freeuni.com");

        Booking booking = new Booking();
        booking.setId(new BookingID(taker.getId(), slot.getSlotId()));
        booking.setUser(taker);
        booking.setSlot(slot);
        booking.setStatus("PENDING");
        bookingRepository.save(booking);

        Booking found = bookingRepository.findById(new BookingID(taker.getId(), slot.getSlotId())).orElseThrow();
        assertEquals("PENDING", found.getStatus());
    }

    @Test
    void updatesBookingStatus() {
        User taker = buildUser("taker2@freeuni.com");
        Slot slot = buildSlot("provider2@freeuni.com");

        Booking booking = new Booking();
        booking.setId(new BookingID(taker.getId(), slot.getSlotId()));
        booking.setUser(taker);
        booking.setSlot(slot);
        booking.setStatus("PENDING");
        Booking saved = bookingRepository.save(booking);

        saved.setStatus("CONFIRMED");
        bookingRepository.saveAndFlush(saved);

        Booking found = bookingRepository.findById(saved.getId()).orElseThrow();
        assertEquals("CONFIRMED", found.getStatus());
    }

}
