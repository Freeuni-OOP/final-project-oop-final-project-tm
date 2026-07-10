package com.finalproject.backend.Services;

import com.finalproject.backend.entities.Notification;
import com.finalproject.backend.profile.DTO.NotificationDTO;
import com.finalproject.backend.repositories.NotificationRepository;
import com.finalproject.backend.services.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private final Integer TEST_USER_ID = 1;
    private final String TEST_TEXT = "You have a new follower!";

    @Test
    @DisplayName("Should correctly map and save a new notification")
    void addNotification_SavesNotificationWithCorrectProperties() {
        notificationService.addNotification(TEST_USER_ID, TEST_TEXT);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(notificationCaptor.capture());

        Notification savedNotification = notificationCaptor.getValue();

        assertNotNull(savedNotification);
        assertEquals(TEST_USER_ID, savedNotification.getUserId());
        assertEquals(TEST_TEXT, savedNotification.getText());
        assertFalse(savedNotification.getSeen());
        assertNotNull(savedNotification.getCreated());
        assertTrue(savedNotification.getCreated().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Should return a list of NotificationDTOs mapped from existing entities")
    void getNotification_ReturnsMappedDtoList() {
        Notification mockNotification = new Notification();
        mockNotification.setId(100);
        mockNotification.setUserId(TEST_USER_ID);
        mockNotification.setText(TEST_TEXT);
        mockNotification.setCreated(LocalDateTime.now());
        mockNotification.setSeen(false);

        when(notificationRepository.findAllByUserIdOrderByCreatedDesc(TEST_USER_ID))
                .thenReturn(List.of(mockNotification));

        List<NotificationDTO> result = notificationService.getNotification(TEST_USER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());

        NotificationDTO dto = result.get(0);
        assertEquals(mockNotification.getId(), dto.getId());
        assertEquals(mockNotification.getUserId(), dto.getUserId());
        assertEquals(mockNotification.getText(), dto.getText());
        assertEquals(mockNotification.getCreated(), dto.getCreated());
        assertEquals(mockNotification.getSeen(), dto.getSeen());

        verify(notificationRepository, times(1)).findAllByUserIdOrderByCreatedDesc(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should return an empty list when user has no notifications")
    void getNotification_WhenNoNotifications_ReturnsEmptyList() {
        when(notificationRepository.findAllByUserIdOrderByCreatedDesc(TEST_USER_ID))
                .thenReturn(Collections.emptyList());

        List<NotificationDTO> result = notificationService.getNotification(TEST_USER_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepository, times(1)).findAllByUserIdOrderByCreatedDesc(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should update all matching notifications to seen status")
    void setSeen_UpdatesAllUserNotificationsToTrue() {
        Notification n1 = new Notification();
        n1.setSeen(false);
        Notification n2 = new Notification();
        n2.setSeen(false);

        List<Notification> mockNotifications = List.of(n1, n2);

        when(notificationRepository.findAllByUserId(TEST_USER_ID)).thenReturn(mockNotifications);

        notificationService.setSeen(TEST_USER_ID);

        assertTrue(n1.getSeen());
        assertTrue(n2.getSeen());

        verify(notificationRepository, times(1)).findAllByUserId(TEST_USER_ID);
        verify(notificationRepository, times(1)).saveAll(mockNotifications);
    }

    @Test
    @DisplayName("Should execute gracefully without errors when user has no notifications to update")
    void setSeen_WhenNoNotifications_ExecutesSuccessfully() {
        when(notificationRepository.findAllByUserId(TEST_USER_ID)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> notificationService.setSeen(TEST_USER_ID));
        verify(notificationRepository, times(1)).saveAll(Collections.emptyList());
    }
}