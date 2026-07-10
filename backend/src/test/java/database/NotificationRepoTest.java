package database;
import com.finalproject.backend.BackendApplication;
import com.finalproject.backend.entities.Notification;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.NotificationRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ContextConfiguration(classes = BackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class NotificationRepoTest {
    @Autowired
    private NotificationRepository notificationRepository;

    private Notification buildNotification(Integer userId, String text) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setText(text);
        n.setCreated(LocalDateTime.now());
        n.setSeen(false);
        return n;
    }

    @Test
    void savesAndRetrievesNotification() {
        Notification saved = notificationRepository.save(buildNotification(1, "Your booking was confirmed"));

        Notification found = notificationRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Your booking was confirmed", found.getText());
        assertFalse(found.getSeen());
    }

    @Test
    void marksNotificationAsSeen() {
        Notification saved = notificationRepository.save(buildNotification(2, "New follower"));

        saved.setSeen(true);
        notificationRepository.saveAndFlush(saved);

        Notification found = notificationRepository.findById(saved.getId()).orElseThrow();
        assertTrue(found.getSeen());
    }

}
