package com.finalproject.backend.profile.mockCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class cookieController {
    @PostMapping("/api/login")
    public ResponseEntity<?> login(HttpServletResponse response) {
        // 1. Imagine we authenticated the user and their database ID is 42
        Integer userId = 2;

        // 2. Create the cookie (Convert the Integer ID to a String)
        Cookie userCookie = new Cookie("userId", String.valueOf(userId));

        // 3. Security configurations (Highly Recommended)
        userCookie.setHttpOnly(true);   // Prevents JavaScript from stealing the cookie (mitigates XSS)
        userCookie.setSecure(false);     // Change to 'true' in production when using HTTPS
        userCookie.setPath("/");         // Makes the cookie available to your entire website
        userCookie.setMaxAge(60000); // Expiration time in seconds (e.g., 7 days)

        // 4. Add the cookie to the response
        response.addCookie(userCookie);

        return ResponseEntity.ok("Logged in successfully! Cookie has been set.");
    }
}
