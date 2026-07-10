package com.finalproject.backend.modules.services;

import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import com.finalproject.backend.services.listingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class listingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private listingService listingService;

    @InjectMocks
    private listingController listingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(listingController).build();
    }

    @Test
    void getListings_WithDefaults() throws Exception {
        List<listingResponseDto> mockResponse = Collections.emptyList();
        when(listingService.findListings(any(listingFilterDto.class), eq("id"), eq("ASC")))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/listings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(listingService).findListings(any(listingFilterDto.class), eq("id"), eq("ASC"));
    }

    @Test
    void getListings_WithCustomParameters() throws Exception {
        List<listingResponseDto> mockResponse = Collections.emptyList();
        String sortType = "price";
        String direction = "DESC";

        when(listingService.findListings(any(listingFilterDto.class), eq(sortType), eq(direction)))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/listings")
                        .param("sortType", sortType)
                        .param("direction", direction))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(listingService).findListings(any(listingFilterDto.class), eq(sortType), eq(direction));
    }
}