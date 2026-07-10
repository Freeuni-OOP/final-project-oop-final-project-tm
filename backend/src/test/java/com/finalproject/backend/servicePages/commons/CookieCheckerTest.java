package com.finalproject.backend.servicePages.commons;

import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.UserService;
import com.finalproject.backend.servicePages.exception.AuthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CookieCheckerTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenCreator tokenCreator;

    @InjectMocks
    private CookieChecker cookieChecker;

    @Test
    void nullCookieThrowsWithMissingCookieCode() {
        assertThatThrownBy(() -> cookieChecker.checkCookie(null))
                .isInstanceOfSatisfying(AuthException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("Cookie Missing");
                    assertThat(e.getErrorCode()).isEqualTo("AUTH_COOKIE_MISSING");
                });
    }

    @Test
    void emptyCookieThrowsWithMissingCookieCode() {
        assertThatThrownBy(() -> cookieChecker.checkCookie(""))
                .isInstanceOfSatisfying(AuthException.class, e ->
                        assertThat(e.getErrorCode()).isEqualTo("AUTH_COOKIE_MISSING"));
    }

    @Test
    void unresolvableTokenThrowsWithInvalidTokenCode() {
        when(tokenCreator.validateTokenAndGetEmail("bad-token")).thenReturn(null);

        assertThatThrownBy(() -> cookieChecker.checkCookie("bad-token"))
                .isInstanceOfSatisfying(AuthException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("Invalid Token");
                    assertThat(e.getErrorCode()).isEqualTo("AUTH_TOKEN_INVALID");
                });
    }

    @Test
    void validCookieResolvesToTheUserId() {
        when(tokenCreator.validateTokenAndGetEmail("good-token")).thenReturn("ana@example.com");
        when(userService.getIdByEmail("ana@example.com")).thenReturn(7);

        assertThat(cookieChecker.checkCookie("good-token")).isEqualTo(7);
    }
}
