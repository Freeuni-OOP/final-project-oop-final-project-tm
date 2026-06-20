package com.finalproject.backend.mockLoginedChecker; // Note: Change this to match your actual package name!

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class TokenCreator {

    // ==========================================
    // 1. CREATING THE SESSION (The Dummy Login)
    // ==========================================
    @PostMapping("/dummy-login")
    public ResponseEntity<String> createMockSession() {

        // Create a hardcoded fake token
        String fakeToken = "mock_super_secret_token_123";

        // Build the HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("auth_token", fakeToken)
                .httpOnly(true)
                .secure(false) // Keep false for localhost testing
                .path("/")     // Allow the whole React app to see it
                .maxAge(86400) // Lasts for 1 day
                .build();

        // Send it back to the browser
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Mock session created! You have the cookie.");
    }
}