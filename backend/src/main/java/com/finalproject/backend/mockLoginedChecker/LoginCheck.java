package com.finalproject.backend.mockLoginedChecker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginCheck {


    // 1. Your fetch command hits this exact method
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(


            // 2. Spring automatically grabs the cookie named "auth_token" from the request
            @CookieValue(name = "auth_token", required = false) String token) {

        // 3. If there is no token at all, instantly return a 401 Unauthorized (fetch fails)
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }

        try {
            // 4. Validate the token mathematically using your Secret Key
            // (You will use a library like 'jjwt' to do this actual checking)

            boolean isValid = false; //jwtService.validateToken(token);

            if(token.equals("mock_super_secret_token_123")) {
                isValid = true;
            }

            if (isValid) {
                // 5. Token is good! Return 200 OK.
                // This makes response.ok = true in your React fetch!
                return ResponseEntity.ok("User is verified");
            } else {
                // Token is tampered with or expired
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

        } catch (Exception e) {
            // Something went wrong, reject them
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Verification failed");
        }
    }
}