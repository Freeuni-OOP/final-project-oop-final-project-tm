package com.finalproject.backend.servicePages.commons;

import com.finalproject.backend.servicePages.exception.AuthException;
import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CookieChecker {
    public final UserService userService;
    public final TokenCreator tokenCreator;

    public CookieChecker(UserService userService, TokenCreator tokenCreator) {
        this.userService = userService;
        this.tokenCreator = tokenCreator;
    }

    public Integer checkCookie(String userCookie) {
        if (userCookie == null || userCookie.isEmpty()) {
            System.out.println("Cookie Missing");
            throw new AuthException("Cookie Missing", "AUTH_COOKIE_MISSING");
        }
        String mail = tokenCreator.validateTokenAndGetEmail(userCookie);
        System.out.println("Email: " + mail + "\n");
        if (mail == null) {
            throw new AuthException("Invalid Token", "AUTH_TOKEN_INVALID");
        }
        return userService.getIdByEmail(mail);

    }
}
