package com.finalproject.backend.Profile.controllers;

import com.finalproject.backend.profile.FollowController;
import com.finalproject.backend.profile.FollowService;
import com.finalproject.backend.profile.UserService;
import com.finalproject.backend.services.CookieService;
import com.finalproject.backend.services.NotificationService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
        })
class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CookieService cookieService;

    @MockitoBean
    private FollowService followService;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should return follower count successfully")
    void getFollowersCountSuccess() throws Exception {
        when(followService.getFollowers(1)).thenReturn(42);

        mockMvc.perform(get("/api/profile/follow/follower/count/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));

        verify(followService, times(1)).getFollowers(1);
    }

    @Test
    @DisplayName("Should process new follow and return updated count")
    void newFollowSuccess() throws Exception {
        Cookie cookie = new Cookie("jwt_token", "valid-token");
        when(cookieService.checkCookie("valid-token")).thenReturn(10);
        when(followService.getFollowers(20)).thenReturn(5);
        when(userService.getName(10)).thenReturn("John Doe");
        when(userService.getName(20)).thenReturn("Jane Smith");

        mockMvc.perform(post("/api/profile/follow/new/20").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(cookieService, times(1)).checkCookie("valid-token");
        verify(followService, times(1)).follow(10, 20);
        verify(followService, times(1)).getFollowers(20);
        verify(notificationService, times(2)).addNotification(anyInt(), anyString());
    }

    @Test
    @DisplayName("Should return following count successfully")
    void getFollowingCountSuccess() throws Exception {
        when(followService.getFollowings(1)).thenReturn(15);

        mockMvc.perform(get("/api/profile/follow/following/count/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));

        verify(followService, times(1)).getFollowings(1);
    }

    @Test
    @DisplayName("Should process unfollow and return updated count")
    void loseFollowSuccess() throws Exception {
        Cookie cookie = new Cookie("jwt_token", "valid-token");
        when(cookieService.checkCookie("valid-token")).thenReturn(10);
        when(followService.getFollowers(20)).thenReturn(4);
        when(userService.getName(10)).thenReturn("John Doe");
        when(userService.getName(20)).thenReturn("Jane Smith");

        mockMvc.perform(post("/api/profile/follow/delete/20").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));

        verify(cookieService, times(1)).checkCookie("valid-token");
        verify(followService, times(1)).unfollow(10, 20);
        verify(followService, times(1)).getFollowers(20);
        verify(notificationService, times(2)).addNotification(anyInt(), anyString());
    }

    @Test
    @DisplayName("Should return true when viewer is following target user")
    void isFollowingTrue() throws Exception {
        Cookie cookie = new Cookie("jwt_token", "valid-token");
        when(cookieService.checkCookie("valid-token")).thenReturn(10);
        when(followService.isAFollower(10, 20)).thenReturn(true);

        mockMvc.perform(get("/api/profile/follow/isfollowing/20").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(cookieService, times(1)).checkCookie("valid-token");
        verify(followService, times(1)).isAFollower(10, 20);
    }

    @Test
    @DisplayName("Should return false when viewer is not following target user")
    void isFollowingFalse() throws Exception {
        Cookie cookie = new Cookie("jwt_token", "valid-token");
        when(cookieService.checkCookie("valid-token")).thenReturn(10);
        when(followService.isAFollower(10, 20)).thenReturn(false);

        mockMvc.perform(get("/api/profile/follow/isfollowing/20").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(cookieService, times(1)).checkCookie("valid-token");
        verify(followService, times(1)).isAFollower(10, 20);
    }
}