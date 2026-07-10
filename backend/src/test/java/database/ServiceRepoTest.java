package database;


import com.finalproject.backend.BackendApplication;
import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = BackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ServiceRepoTest {

    @Autowired
    private ServiceRepository ServiceRepository;

    @Autowired
    private UserRepository userRepo;

    private User buildProvider(String email) {
        User u = new User();
        u.setFirstName("Provider");
        u.setLastName("Person");
        u.setEmail(email);
        u.setPassHash("123");
        u.setEnabled(true);
        return userRepo.save(u);
    }

    private Service buildService(User provider) {
        Service s = new Service();
        s.setProviderId(provider);
        s.setTitle("Piano Lessons");
        s.setBio("Learn piano fast");
        s.setImagePath("");
        s.setTimePosted(LocalDateTime.now());
        s.setPrice(45.0);
        s.setCategory("Music");
        s.setAddress("123 Main St");
        s.setMaxCapacity(15);
        s.setStar(5);
        s.setActive(true);

        return s;
    }

    @Test
    void savesAndGetService() {
        User provider = buildProvider("provider1@example.com");
        Service saved = ServiceRepository.save(buildService(provider));

        Service found = ServiceRepository.findById(saved.getId()).orElseThrow();

        assertEquals("Piano Lessons", found.getTitle());
        assertEquals(found.getProviderId().getId(), provider.getId());
    }

    @Test
    void updatesServicePrice() {
        User provider = buildProvider("provider2@example.com");
        Service saved = ServiceRepository.save(buildService(provider));

        saved.setPrice(60.0);
        ServiceRepository.saveAndFlush(saved);

        Service found = ServiceRepository.findById(saved.getId()).orElseThrow();
        assertEquals(60.0, found.getPrice());
    }

}