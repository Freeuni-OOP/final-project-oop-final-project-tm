package com.finalproject.backend.history;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.profile.DTO.ServiceDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ServiceDTOTest {

    @Test
    void testConstructorsAndSetters() {
        // Test Constructor
        ServiceDTO dto = new ServiceDTO();
        assertNotNull(dto);

        // Test Setters
        LocalDateTime now = LocalDateTime.now();
        dto.setId(1);
        dto.setProviderId(2);
        dto.setProviderName("Test Provider");
        dto.setTitle("Test Title");
        dto.setBio("Test Bio");
        dto.setImagePath("path/img.jpg");
        dto.setTimePosted(now);
        dto.setPrice(19.99);
        dto.setCategory("Test Category");
        dto.setAddress("123 Test St");
        dto.setMaxCapacity(5);
        dto.setActive(true);
        dto.setStar(5);

        // Test getters
        assertEquals(1, dto.getId());
        assertEquals(2, dto.getProviderId());
        assertEquals("Test Provider", dto.getProviderName());
        assertEquals("Test Title", dto.getTitle());
        assertEquals("Test Bio", dto.getBio());
        assertEquals("path/img.jpg", dto.getImagePath());
        assertEquals(now, dto.getTimePosted());
        assertEquals(19.99, dto.getPrice());
        assertEquals("Test Category", dto.getCategory());
        assertEquals("123 Test St", dto.getAddress());
        assertEquals(5, dto.getMaxCapacity());
        assertTrue(dto.getActive());
        assertEquals(5, dto.getStar());
    }

    @Test
    void testEntityConstructor() {
        // Prepare Entity
        User user = new User();
        user.setId(10);

        Service entity = new Service();
        entity.setId(1);
        entity.setProviderId(user);
        entity.setTitle("Entity Title");
        ServiceDTO dto = new ServiceDTO(entity);

        assertEquals(1, dto.getId());
        assertEquals(10, dto.getProviderId());
        assertEquals("Entity Title", dto.getTitle());
    }

    @Test
    void testEntityConstructorWithNullProvider() {
        Service entity = new Service();
        entity.setProviderId(null);

        ServiceDTO dto = new ServiceDTO(entity);

        assertNull(dto.getProviderId());
    }
}