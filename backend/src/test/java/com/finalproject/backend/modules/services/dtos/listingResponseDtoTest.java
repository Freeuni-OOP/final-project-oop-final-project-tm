package com.finalproject.backend.modules.services.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class listingResponseDtoTest {

    @Test
    void testDefaultConstructorAndSetters() {
        listingResponseDto dto = new listingResponseDto();

        dto.setId(1);
        dto.setTitle("Test Title");
        dto.setCategory("Service");
        dto.setDescription("Test Description");
        dto.setPrice(99.99);
        dto.setImageUrl("http://example.com/image.jpg");

        assertEquals(1, dto.getId());
        assertEquals("Test Title", dto.getTitle());
        assertEquals("Service", dto.getCategory());
        assertEquals("Test Description", dto.getDescription());
        assertEquals(99.99, dto.getPrice());
        assertEquals("http://example.com/image.jpg", dto.getImageUrl());
    }

    @Test
    void testParameterizedConstructor() {
        listingResponseDto dto = new listingResponseDto(2, "Param Title", "Category", "Desc", 50.0, "url");

        assertEquals(2, dto.getId());
        assertEquals("Param Title", dto.getTitle());
        assertEquals("Category", dto.getCategory());
        assertEquals("Desc", dto.getDescription());
        assertEquals(50.0, dto.getPrice());
        assertEquals("url", dto.getImageUrl());
    }
}