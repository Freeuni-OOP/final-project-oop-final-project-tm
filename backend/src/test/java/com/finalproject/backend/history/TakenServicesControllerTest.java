package com.finalproject.backend.history;

import com.finalproject.backend.profile.DTO.ServiceDTO;
import com.finalproject.backend.profile.history.TakenServicesController;
import com.finalproject.backend.profile.history.TakenServicesService;
import com.finalproject.backend.services.CookieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.Cookie;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TakenServicesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TakenServicesService takenServicesService;

    @Mock
    private CookieService cookieService;

    @InjectMocks
    private TakenServicesController takenServicesController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(takenServicesController).build();
    }

    @Test
    void getOfferedServices_ShouldReturnList() throws Exception {
        Integer id = 1;
        List<ServiceDTO> mockServices = Collections.singletonList(new ServiceDTO());

        when(takenServicesService.getOfferedServices(id)).thenReturn(mockServices);

        mockMvc.perform(get("/api/profile/services/offered/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(takenServicesService, times(1)).getOfferedServices(id);
    }

    @Test
    void getRegisteredServices_ShouldReturnList() throws Exception {
        String token = "test-token";
        int userId = 123;
        List<ServiceDTO> mockServices = Collections.singletonList(new ServiceDTO());

        when(cookieService.checkCookie(token)).thenReturn(userId);
        when(takenServicesService.getRegisteredServices(userId)).thenReturn(mockServices);

        mockMvc.perform(get("/api/profile/services/registered")
                        .cookie(new Cookie("jwt_token", token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(cookieService, times(1)).checkCookie(token);
        verify(takenServicesService, times(1)).getRegisteredServices(userId);
    }
}