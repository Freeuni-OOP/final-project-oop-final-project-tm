package com.finalproject.backend.Profile.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.backend.profile.DTO.ProfileDTO;
import com.finalproject.backend.profile.UserController;
import com.finalproject.backend.profile.UserService;
import com.finalproject.backend.services.CookieService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CookieService cookieService;

    private ProfileDTO sampleProfile;

    @BeforeEach
    void setUp() {
        sampleProfile = new ProfileDTO(42, "John", "Doe", "john.doe@example.com", "Hello world!", "avatar.png");
    }

    @Test
    void getPersonalProfile_ShouldReturnProfile_WhenCookieIsValid() throws Exception {
        String mockToken = "valid-jwt-token";
        when(cookieService.checkCookie(mockToken)).thenReturn(42);
        when(userService.getUser(42)).thenReturn(sampleProfile);

        mockMvc.perform(get("/api/profile/")
                        .cookie(new Cookie("jwt_token", mockToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.aboutMe").value("Hello world!"));

        verify(cookieService, times(1)).checkCookie(mockToken);
        verify(userService, times(1)).getUser(42);
    }

    @Test
    void getPublicProfile_ShouldReturnPair_WithProfileAndCookieId() throws Exception {
        int targetUserId = 99;
        String mockToken = "viewer-jwt-token";
        int viewerId = 42;

        when(cookieService.checkCookie(mockToken)).thenReturn(viewerId);
        when(userService.getUser(targetUserId)).thenReturn(sampleProfile);

        mockMvc.perform(get("/api/profile/{id}", targetUserId)
                        .cookie(new Cookie("jwt_token", mockToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first.id").value(42))
                .andExpect(jsonPath("$.second").value(viewerId));
    }

    @Test
    void updateUserProfile_ShouldSucceed_WhenIdsMatch() throws Exception {
        String mockToken = "valid-jwt-token";
        when(cookieService.checkCookie(mockToken)).thenReturn(42);

        mockMvc.perform(post("/api/profile/update")
                        .cookie(new Cookie("jwt_token", mockToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProfile)))
                .andExpect(status().isOk());

        verify(userService, times(1)).UpdatePublicUser(any(ProfileDTO.class));
    }


}