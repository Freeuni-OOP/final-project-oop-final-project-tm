package com.finalproject.backend.Profile.DTOtest;

import com.finalproject.backend.profile.DTO.NotificationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDTOTest {

    @Test
    @DisplayName("Should correctly initialize via no-args constructor and setters")
    void testNoArgsConstructorAndSetters() {
        NotificationDTO dto = new NotificationDTO();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(1);
        dto.setUserId(10);
        dto.setText("Test Notification");
        dto.setCreated(now);
        dto.setSeen(true);

        assertEquals(1, dto.getId());
        assertEquals(10, dto.getUserId());
        assertEquals("Test Notification", dto.getText());
        assertEquals(now, dto.getCreated());
        assertTrue(dto.getSeen());
    }

    @Test
    @DisplayName("Should correctly initialize via all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        NotificationDTO dto = new NotificationDTO(2, 20, "Another Notification", now, false);

        assertEquals(2, dto.getId());
        assertEquals(20, dto.getUserId());
        assertEquals("Another Notification", dto.getText());
        assertEquals(now, dto.getCreated());
        assertFalse(dto.getSeen());
    }
}