package database;


import com.finalproject.backend.BackendApplication;
import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.SlotsRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ContextConfiguration(classes = BackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SlotRepoTest {
    @Autowired
    private SlotsRepository slotRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    private Service buildService(String email) {
        User provider = new User();
        provider.setFirstName("Provider");
        provider.setLastName("Person");
        provider.setEmail(email);
        provider.setPassHash("123");
        provider.setEnabled(true);
        userRepository.save(provider);

        Service s = new Service();
        s.setProviderId(provider);
        s.setTitle("Yoga Class");
        s.setPrice(20.0);
        s.setCategory("yoga");
        return serviceRepository.save(s);
    }

    @Test
    void savesAndGetSlot() {
        Service service = buildService("slotprovider1@freeuni.com");

        Slot slot = new Slot();
        slot.setServiceId(service);
        slot.setStartTime(LocalDateTime.now());
        slot.setEndTime(LocalDateTime.now().plusHours(1));
        Slot saved = slotRepository.save(slot);

        Slot found = slotRepository.findById(saved.getSlotId()).orElseThrow();
        assertEquals(found.getServiceId().getId(), service.getId());
    }

    @Test
    void updatesSlotTimes() {
        Service service = buildService("slotprovider2@freeuni.com");

        Slot slot = new Slot();
        slot.setServiceId(service);
        slot.setStartTime(LocalDateTime.now());
        slot.setEndTime(LocalDateTime.now().plusHours(1));
        Slot saved = slotRepository.save(slot);

        LocalDateTime newEnd = saved.getEndTime().plusHours(2);
        saved.setEndTime(newEnd);
        slotRepository.saveAndFlush(saved);

        Slot found = slotRepository.findById(saved.getSlotId()).orElseThrow();
        assertEquals(found.getEndTime(), newEnd);
    }

}
