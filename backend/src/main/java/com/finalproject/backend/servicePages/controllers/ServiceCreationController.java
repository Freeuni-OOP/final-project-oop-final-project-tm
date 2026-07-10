package com.finalproject.backend.servicePages.controllers;

import com.finalproject.backend.profile.UserService;
import com.finalproject.backend.servicePages.commons.CookieChecker;
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
    private final CookieChecker cookieChecker;
    public ServiceCreationController(ServiceCreationManager manager,
                                     TokenCreator tokenCreator,
                                     UserService userService,
                                     CookieChecker cookieChecker) {
        this.manager = manager;
        this.tokenCreator = tokenCreator;
        this.userService = userService;
        this.cookieChecker = cookieChecker;
    }

    @PostMapping("")
    public ResponseEntity<?> createService(
            @Valid @ModelAttribute ServiceCreationRequest request,
            @CookieValue(value = "jwt_token", required = false) String userCookie) {

        try {
            Integer userId = cookieChecker.checkCookie(userCookie);

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