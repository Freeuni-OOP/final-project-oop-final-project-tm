package com.finalproject.backend.servicePages.controllers;

import com.finalproject.backend.profile.UserService;
import com.finalproject.backend.servicePages.exception.AuthException;
import com.finalproject.backend.servicePages.logic.ServiceCreationManager;
import com.finalproject.backend.servicePages.model.ServiceCreationRequest;
import com.finalproject.backend.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.finalproject.backend.login_register.config.TokenCreator;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/service-creation")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ServiceCreationController {

    private final ServiceCreationManager manager;
    private final TokenCreator tokenCreator;
    private final UserService userService;
    public ServiceCreationController(ServiceCreationManager manager, TokenCreator tokenCreator, UserService userService) {
        this.manager = manager;
        this.tokenCreator = tokenCreator;
        this.userService = userService;
    }
    // Checks if user cookie is valid and returns user id
    private Integer checkCookie(String userCookie) {
        if(userCookie == null || userCookie.isEmpty()) {
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

    @PostMapping("")
    public ResponseEntity<?> createService(
            @Valid @ModelAttribute ServiceCreationRequest request,
            @CookieValue(value = "jwt_token", required = false) String userCookie) {

        try {
            Integer userId = checkCookie(userCookie);

            int newServiceId = manager.postServiceInformation(request, userId);

            return ResponseEntity.ok(Map.of(
                    "message", "Service created successfully",
                    "serviceId", newServiceId
            ));
        } catch (AuthException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", e.getMessage(),
                    "errorCode", e.getErrorCode()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to process file"));
        }
    }
}