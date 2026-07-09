package com.finalproject.backend.services;

import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.UserService;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    private final UserService userService;
    private final TokenCreator tokenCreator;

    public CookieService(UserService userService, TokenCreator tokenCreator) {
        this.userService = userService;
        this.tokenCreator = tokenCreator;
    }

    public Integer checkCookie(String userCookie) {
        if(userCookie == null || userCookie.isEmpty()) {
            throw new RuntimeException("Cookie Missing");
        }

        String mail = tokenCreator.validateTokenAndGetEmail(userCookie);
        return userService.getIdByEmail(mail);
    }
}
