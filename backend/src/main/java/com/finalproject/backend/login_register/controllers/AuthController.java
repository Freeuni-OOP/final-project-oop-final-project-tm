package com.finalproject.backend.login_register.controllers;

import com.finalproject.backend.login_register.DTO.LoginRequest;
import com.finalproject.backend.login_register.DTO.RegisterRequest;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.login_register.services.EmailSender;
import com.finalproject.backend.repositories.UserRepository;
import com.finalproject.backend.login_register.config.TokenCreator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final TokenCreator tokenCreator;

    public AuthController(UserRepository userRepository, EmailSender emailSender, TokenCreator tokenCreator) {
        this.userRepository = userRepository;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.emailSender = emailSender;
        this.tokenCreator = tokenCreator;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        String IP = request.getRemoteAddr();

        //searches if the given user exists
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        User user = userOptional.get();

        //compares the password and adds failed attempts to log in
        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassHash());
        if (!matches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        //an unauthorized user should not be able to log in
        if (!user.getEnabled()) {
            String freshCode = emailSender.generateCode();
            user.setVerificationCode(freshCode);
            userRepository.save(user);
            try {
                emailSender.sendEmail(user.getEmail(), freshCode, "Verify your Registration", "Thank you for registering! Your 6-digit verification code is: ");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed to send new email.");
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("NOT_VERIFIED: Please enter the new verification code sent to your email.");
        }

        setAuthCookie(response, user.getEmail());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entered Email is already in use, please log in instead!");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirst_name());
        user.setLastName(registerRequest.getLast_name());
        user.setEnabled(false);

        //saves the encoded password to ensure safety
        String passHash = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassHash(passHash);

        String verificationCode = emailSender.generateCode();
        user.setVerificationCode(verificationCode);

        try {
            emailSender.sendEmail(user.getEmail(), verificationCode,  "Verify your Registration", "Thank you for registering! Your 6-digit verification code is: ");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification email.");
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful! Please check your email for verification code.");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String email = request.get("email");
        String code = request.get("code");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        //confirms the correct verification
        User user = userOptional.get();
        if (user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.save(user);

            setAuthCookie(response, user.getEmail());

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code.");
        }
    }

    //makes sure that if a user refreshed the page they would still be logged in or cases like that
    @GetMapping("/verify-session")
    public ResponseEntity<?> verifySession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    String email = tokenCreator.validateTokenAndGetEmail(token);

                    if (email != null) {
                        Optional<User> userOptional = userRepository.findByEmail(email);
                        if (userOptional.isPresent()) {
                            return ResponseEntity.ok(userOptional.get());
                        }
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired or missing cookie");
    }

    private void setAuthCookie(HttpServletResponse response, String email) {
        String token = tokenCreator.generateToken(email);
        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}