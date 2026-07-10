package database;

import com.finalproject.backend.BackendApplication;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ContextConfiguration(classes = BackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepoTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndGetUser() {
        User user = new User();
        user.setEmail("oop@freeuni.com");
        user.setFirstName("will");
        user.setLastName("Po");
        user.setPassHash("hashedpassword123");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertTrue(userRepository.findById(savedUser.getId()).isPresent());
    }

    @Test
    public void testEmailUniqueConstraint() {
        User user1 = buildUser("unique@freeuni.com");

        userRepository.saveAndFlush(user1);

        User user2 = buildUser("unique@freeuni.com");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }

    @Test
    void updatesUser() {
        User saved = userRepository.save(buildUser("bob@freeuni.com"));

        saved.setAboutMe("Updated bio");
        userRepository.saveAndFlush(saved);

        User found = userRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Updated bio", found.getAboutMe());
    }


    private User buildUser(String email) {
        User u = new User();
        u.setFirstName("Test");
        u.setLastName("User");
        u.setEmail(email);
        u.setAboutMe("Hello");
        u.setPassHash("123");
        return u;
    }

}