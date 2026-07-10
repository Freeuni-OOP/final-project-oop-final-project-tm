package com.finalproject.backend.servicePages.controllers;

import com.finalproject.backend.servicePages.commons.CookieChecker;
import com.finalproject.backend.servicePages.exception.AuthException;
import com.finalproject.backend.servicePages.logic.ServiceManager;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ServiceControllerTest {

    @Mock
    private ServiceManager serviceManager;

    @Mock
    private CookieChecker cookieChecker;

    private MockMvc mockMvc;

    private static final Cookie AUTH = new Cookie("jwt_token", "token");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ServiceController(serviceManager, cookieChecker))
                .build();
    }

    @Test
    void serviceLookupReturnsTheServiceJson() throws Exception {
        when(serviceManager.getServiceInformation(5)).thenReturn(Map.of(
                "serviceId", 5, "serviceTitle", "Guitar Lessons"));

        mockMvc.perform(get("/api/services/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceTitle").value("Guitar Lessons"));
    }

    @Test
    void serviceLookupReturns404ForUnknownService() throws Exception {
        when(serviceManager.getServiceInformation(99)).thenReturn(null);

        mockMvc.perform(get("/api/services/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void starringRequiresAValidCookie() throws Exception {
        when(cookieChecker.checkCookie(null))
                .thenThrow(new AuthException("Cookie Missing", "AUTH_COOKIE_MISSING"));

        mockMvc.perform(post("/api/services/5/star"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cookie Missing"));
    }

    @Test
    void starringAddsAStarForTheCookieUser() throws Exception {
        when(cookieChecker.checkCookie("token")).thenReturn(7);

        mockMvc.perform(post("/api/services/5/star").cookie(AUTH))
                .andExpect(status().isOk());

        verify(serviceManager).addStar(5, 7);
    }

    @Test
    void unstarringRemovesTheStar() throws Exception {
        when(cookieChecker.checkCookie("token")).thenReturn(7);

        mockMvc.perform(delete("/api/services/5/star").cookie(AUTH))
                .andExpect(status().isOk());

        verify(serviceManager).removeStar(5, 7);
    }

    @Test
    void starCountIsReturnedForAService() throws Exception {
        when(serviceManager.getStarNumber(5)).thenReturn(Map.of("starNum", 4));

        mockMvc.perform(get("/api/services/5/star-num"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.starNum").value(4));
    }

    @Test
    void starCountFailureBecomes400() throws Exception {
        when(serviceManager.getStarNumber(99)).thenThrow(new RuntimeException("Service Not Found"));

        mockMvc.perform(get("/api/services/99/star-num"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Service Not Found"));
    }

    @Test
    void starStatusIsReturnedForTheCookieUser() throws Exception {
        when(cookieChecker.checkCookie("token")).thenReturn(7);
        when(serviceManager.getStarEssence(5, 7)).thenReturn(Map.of("stared", true));

        mockMvc.perform(get("/api/services/5/star").cookie(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stared").value(true));
    }

    @Test
    void starStatusDefaultsToFalseWithoutAValidCookie() throws Exception {
        when(cookieChecker.checkCookie(null))
                .thenThrow(new AuthException("Cookie Missing", "AUTH_COOKIE_MISSING"));

        mockMvc.perform(get("/api/services/5/star"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stared").value(false));
    }

    @Test
    void profileImageLookupReturnsTheImagePath() throws Exception {
        when(serviceManager.getServiceImagePath(5)).thenReturn(Map.of("imagePath", "img.png"));

        mockMvc.perform(get("/api/services/profile/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imagePath").value("img.png"));
    }

    @Test
    void profileImageLookupReturns404ForUnknownService() throws Exception {
        when(serviceManager.getServiceImagePath(99)).thenReturn(null);

        mockMvc.perform(get("/api/services/profile/99"))
                .andExpect(status().isNotFound());
    }
}
