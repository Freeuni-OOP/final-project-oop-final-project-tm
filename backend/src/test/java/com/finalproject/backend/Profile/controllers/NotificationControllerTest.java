package com.finalproject.backend.Profile.controllers;

import com.finalproject.backend.profile.DTO.NotificationDTO;
import com.finalproject.backend.profile.NotificationController;
import com.finalproject.backend.services.CookieService;
import com.finalproject.backend.services.NotificationService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NotificationController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CookieService cookieService;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void getNotifications_ShouldReturnNotificationList_WhenCookieIsValid() throws Exception {
        String mockToken = "valid-token";
        int mockUserId = 1;
        List<NotificationDTO> mockNotifications = List.of();

        when(cookieService.checkCookie(mockToken)).thenReturn(mockUserId);
        when(notificationService.getNotification(mockUserId)).thenReturn(mockNotifications);

        mockMvc.perform(get("/api/profile/notification/get")
                        .cookie(new Cookie("jwt_token", mockToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(cookieService, times(1)).checkCookie(mockToken);
        verify(notificationService, times(1)).getNotification(mockUserId);
    }

    @Test
    void getNotifications_ShouldReturnBadRequest_WhenCookieIsMissing() throws Exception {
        mockMvc.perform(get("/api/profile/notification/get"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cookieService);
        verifyNoInteractions(notificationService);
    }

    @Test
    void seeMessages_ShouldReturnOk_WhenCookieIsValid() throws Exception {
        String mockToken = "valid-token";
        int mockUserId = 1;

        when(cookieService.checkCookie(mockToken)).thenReturn(mockUserId);

        mockMvc.perform(post("/api/profile/notification/seen")
                        .cookie(new Cookie("jwt_token", mockToken)))
                .andExpect(status().isOk());

        verify(cookieService, times(1)).checkCookie(mockToken);
        verify(notificationService, times(1)).setSeen(mockUserId);
    }

    @Test
    void seeMessages_ShouldReturnBadRequest_WhenCookieIsMissing() throws Exception {
        mockMvc.perform(post("/api/profile/notification/seen"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cookieService);
        verifyNoInteractions(notificationService);
    }
}