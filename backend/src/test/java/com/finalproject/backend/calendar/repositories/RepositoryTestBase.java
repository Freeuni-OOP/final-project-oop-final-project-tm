package com.finalproject.backend.calendar.repositories;

import com.finalproject.backend.entities.Booking;
import com.finalproject.backend.entities.BookingID;
import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class RepositoryTestBase {

    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("book_to");

    static {
        MYSQL.start();
    }

    @Autowired
    protected TestEntityManager em;

    private static final AtomicInteger uniqueEmail = new AtomicInteger();

    protected User newUser() {
        User user = new User();
        user.setFirstName("Repo");
        user.setLastName("Tester");
        user.setEmail("repo-test-" + uniqueEmail.incrementAndGet() + "@example.com");
        user.setPassHash("hash");
        return em.persist(user);
    }

    protected Service newService(User provider, Integer maxCapacity) {
        Service service = new Service();
        service.setProviderId(provider);
        service.setTitle("Repo Test Service");
        service.setPrice(10.0);
        service.setMaxCapacity(maxCapacity);
        service.setActive(true);
        service.setStar(0);
        return em.persist(service);
    }

    protected Slot newSlot(Service service, LocalDateTime start, LocalDateTime end) {
        Slot slot = new Slot();
        slot.setServiceId(service);
        slot.setStartTime(start);
        slot.setEndTime(end);
        return em.persist(slot);
    }

    protected Booking newBooking(User taker, Slot slot, String status) {
        Booking booking = new Booking();
        booking.setId(new BookingID(taker.getId(), slot.getSlotId()));
        booking.setUser(taker);
        booking.setSlot(slot);
        booking.setStatus(status);
        return em.persist(booking);
    }
}
