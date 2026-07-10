package com.finalproject.backend.servicePages.controllers;

import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.UserService;
import com.finalproject.backend.servicePages.commons.CookieChecker;
import com.finalproject.backend.servicePages.exception.AuthException;
import com.finalproject.backend.servicePages.logic.ServiceCreationManager;
import com.finalproject.backend.servicePages.model.ServiceCreationRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ServiceCreationControllerTest {

    @Mock
    private ServiceCreationManager manager;

    @Mock
    private TokenCreator tokenCreator;

    @Mock
    private UserService userService;

    @Mock
    private CookieChecker cookieChecker;

    private MockMvc mockMvc;

    private static final Cookie AUTH = new Cookie("jwt_token", "token");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ServiceCreationController(
                        manager, tokenCreator, userService, cookieChecker))
                .build();
    }

    private static MockMultipartHttpServletRequestBuilder validCreationRequest() {
        return creationRequest(Map.of());
    }

    private static MockMultipartHttpServletRequestBuilder creationRequest(Map<String, String> overrides) {
        Map<String, String> params = new LinkedHashMap<>(Map.of(
                "title", "Guitar Lessons",
                "category", "Music",
                "place", "Tbilisi",
                "bio", "Learn guitar",
                "price", "45.50",
                "date", "2030-01-07T10:00:00Z",
                "maxCapacity", "2"));
        params.putAll(overrides);
        MockMultipartHttpServletRequestBuilder builder = multipart("/api/service-creation");
        params.forEach((key, value) -> builder.param(key, value));
        return builder;
    }

    @Test
    void creatingAServiceReturnsItsId() throws Exception {
        when(cookieChecker.checkCookie("token")).thenReturn(7);
        when(manager.postServiceInformation(any(ServiceCreationRequest.class), eq(7))).thenReturn(42);

        mockMvc.perform(validCreationRequest().cookie(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").value(42))
                .andExpect(jsonPath("$.message").value("Service created successfully"));
    }

    @Test
    void missingCookieIs401WithErrorCode() throws Exception {
        when(cookieChecker.checkCookie(null))
                .thenThrow(new AuthException("Cookie Missing", "AUTH_COOKIE_MISSING"));

        mockMvc.perform(validCreationRequest())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Cookie Missing"))
                .andExpect(jsonPath("$.errorCode").value("AUTH_COOKIE_MISSING"));
    }

    @Test
    void managerRuntimeFailureIs401WithTheMessage() throws Exception {
        when(cookieChecker.checkCookie("token")).thenReturn(7);
        when(manager.postServiceInformation(any(ServiceCreationRequest.class), eq(7)))
                .thenThrow(new IllegalArgumentException("Invalid price format: abc"));

        mockMvc.perform(validCreationRequest().cookie(AUTH))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid price format: abc"));
    }

    @Test
    void fileProcessingFailureIs500() throws Exception {
        when(cookieChecker.checkCookie("token")).thenReturn(7);
        when(manager.postServiceInformation(any(ServiceCreationRequest.class), eq(7)))
                .thenThrow(new IOException("disk full"));

        mockMvc.perform(validCreationRequest().cookie(AUTH))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Failed to process file"));
    }

    @Test
    void missingTitleFailsValidationWith400() throws Exception {
        mockMvc.perform(creationRequest(Map.of("title", "")).cookie(AUTH))
                .andExpect(status().isBadRequest());
    }

    @Test
    void priceWithTooManyDecimalsFailsValidation() throws Exception {
        mockMvc.perform(creationRequest(Map.of("price", "12.345")).cookie(AUTH))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nonNumericPriceFailsValidation() throws Exception {
        mockMvc.perform(creationRequest(Map.of("price", "abc")).cookie(AUTH))
                .andExpect(status().isBadRequest());
    }

    @Test
    void overlongBioFailsValidation() throws Exception {
        mockMvc.perform(creationRequest(Map.of("bio", "x".repeat(501))).cookie(AUTH))
                .andExpect(status().isBadRequest());
    }

    @Test
    void maxCapacityOutsideItsBoundsFailsValidation() throws Exception {
        mockMvc.perform(creationRequest(Map.of("maxCapacity", "0")).cookie(AUTH))
                .andExpect(status().isBadRequest());
        mockMvc.perform(creationRequest(Map.of("maxCapacity", "101")).cookie(AUTH))
                .andExpect(status().isBadRequest());
    }

    @Test
    void attachedPictureIsBoundToTheRequest() throws Exception {
        when(cookieChecker.checkCookie("token")).thenReturn(7);
        when(manager.postServiceInformation(any(ServiceCreationRequest.class), eq(7))).thenReturn(42);

        mockMvc.perform(validCreationRequest()
                        .file(new MockMultipartFile("profilePicture", "pic.png", "image/png", "img".getBytes()))
                        .cookie(AUTH))
                .andExpect(status().isOk());

        ArgumentCaptor<ServiceCreationRequest> bound = ArgumentCaptor.forClass(ServiceCreationRequest.class);
        verify(manager).postServiceInformation(bound.capture(), eq(7));
        assertThat(bound.getValue().getProfilePicture()).isNotNull();
        assertThat(bound.getValue().getProfilePicture().getOriginalFilename()).isEqualTo("pic.png");
    }
}
