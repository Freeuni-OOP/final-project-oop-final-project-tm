package com.finalproject.backend.services;

import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenCreator tokenCreator;

    @InjectMocks
    private CookieService cookieService;

    private final String VALID_COOKIE = "valid.mock.jwt";
    private final String INVALID_COOKIE = "invalid.mock.jwt";
    private final String TEST_EMAIL = "user@example.com";
    private final Integer TEST_USER_ID = 123;

    @Test
    @DisplayName("Should return -1 when cookie is null")
    void checkCookie_WhenCookieIsNull_ReturnsMinusOne() {
        Integer result = cookieService.checkCookie(null);

        assertEquals(-1, result);
        verifyNoInteractions(tokenCreator, userService);
    }

    @Test
    @DisplayName("Should return -1 when cookie is empty")
    void checkCookie_WhenCookieIsEmpty_ReturnsMinusOne() {
        Integer result = cookieService.checkCookie("");

        assertEquals(-1, result);
        verifyNoInteractions(tokenCreator, userService);
    }

    @Test
    @DisplayName("Should return user ID when cookie and email are valid")
    void checkCookie_WhenCookieIsValid_ReturnsUserId() {
        when(tokenCreator.validateTokenAndGetEmail(VALID_COOKIE)).thenReturn(TEST_EMAIL);
        when(userService.getIdByEmail(TEST_EMAIL)).thenReturn(TEST_USER_ID);

        Integer result = cookieService.checkCookie(VALID_COOKIE);

        assertEquals(TEST_USER_ID, result);
        verify(tokenCreator, times(1)).validateTokenAndGetEmail(VALID_COOKIE);
        verify(userService, times(1)).getIdByEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("Should return -1 when token validation throws an exception")
    void checkCookie_WhenTokenCreatorThrowsException_ReturnsMinusOne() {
        when(tokenCreator.validateTokenAndGetEmail(INVALID_COOKIE))
                .thenThrow(new RuntimeException("Token expired or corrupted"));

        Integer result = cookieService.checkCookie(INVALID_COOKIE);

        assertEquals(-1, result);
        verify(tokenCreator, times(1)).validateTokenAndGetEmail(INVALID_COOKIE);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Should return -1 when user service throws an exception")
    void checkCookie_WhenUserServiceThrowsException_ReturnsMinusOne() {
        when(tokenCreator.validateTokenAndGetEmail(VALID_COOKIE)).thenReturn(TEST_EMAIL);
        when(userService.getIdByEmail(TEST_EMAIL))
                .thenThrow(new RuntimeException("Database connection error"));

        Integer result = cookieService.checkCookie(VALID_COOKIE);

        assertEquals(-1, result);
        verify(tokenCreator, times(1)).validateTokenAndGetEmail(VALID_COOKIE);
        verify(userService, times(1)).getIdByEmail(TEST_EMAIL);
    }
}