package com.finalproject.backend.modules.services.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class listingFilterDtoTest {

    @Test
    void testGettersAndSetters() {
        listingFilterDto dto = new listingFilterDto();

        dto.setText("Search Query");
        dto.setCategory("Technology");
        dto.setMin(10.0);
        dto.setMax(100.0);
        dto.setFavoriteUserId(1L);
        dto.setExcludeFavUserId(2L);

        assertEquals("Search Query", dto.getText());
        assertEquals("Technology", dto.getCategory());
        assertEquals(10.0, dto.getMin());
        assertEquals(100.0, dto.getMax());
        assertEquals(1L, dto.getFavoriteUserId());
        assertEquals(2L, dto.getExcludeFavUserId());
    }
}