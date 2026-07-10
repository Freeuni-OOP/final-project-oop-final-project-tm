package com.finalproject.backend.Profile.DTOtest;

import com.finalproject.backend.profile.DTO.ProfileDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileDTOTest {

    @Test
    @DisplayName("Should correctly initialize via all-args constructor")
    void testAllArgsConstructorAndGetters() {
        ProfileDTO dto = new ProfileDTO(
                1,
                "John",
                "Doe",
                "john.doe@example.com",
                "Software Engineer",
                "/images/john.png"
        );

        assertEquals(1, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals("Software Engineer", dto.getAboutMe());
        assertEquals("/images/john.png", dto.getImagePath());
    }

    @Test
    @DisplayName("Should correctly update values via setters")
    void testSetters() {
        ProfileDTO dto = new ProfileDTO(1, "John", "Doe", "john.doe@example.com", "Old bio", "/old.png");

        dto.setId(2);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setEmail("jane.smith@example.com");
        dto.setAboutMe("New bio");
        dto.setImagePath("/new.png");

        assertEquals(2, dto.getId());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("jane.smith@example.com", dto.getEmail());
        assertEquals("New bio", dto.getAboutMe());
        assertEquals("/new.png", dto.getImagePath());
    }
}