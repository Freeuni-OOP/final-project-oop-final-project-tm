package com.finalproject.backend.servicePages.controllers;

import com.finalproject.backend.servicePages.exception.AuthException;
import com.finalproject.backend.servicePages.logic.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.UserService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ServiceController {

    // 1. Declare the ServiceManager as a private final variable
    private final ServiceManager serviceManager;
    private final TokenCreator tokenCreator;
    private final UserService userService;

    // 2. Inject it via the constructor!
    public ServiceController(ServiceManager serviceManager,
                             TokenCreator tokenCreator, UserService userService) {
        this.serviceManager = serviceManager;
        this.tokenCreator = tokenCreator;
        this.userService = userService;
    }

    // Maps to GET http://localhost:8080/api/services/{id}
    @GetMapping("/{serviceId}")
    public ResponseEntity<?> getServiceById(@PathVariable("serviceId") int serviceId) {

        Map<String, Object> serviceInfo = serviceManager.getServiceInformation(serviceId);

        if(serviceInfo == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the map, which Spring Boot will convert to JSON!
        return ResponseEntity.ok(serviceInfo);
    }

    @PostMapping("/{serviceId}/star")
    public ResponseEntity<?> starService(@PathVariable("serviceId") int serviceId,
                @CookieValue(value = "jwt_token", required = false) String userCookie) {
        try {
            int userId = checkCookie(userCookie);

            Map<String, Object> serviceInfo = serviceManager.addStar(serviceId,userId);

            if(serviceInfo == null) {
                return ResponseEntity.notFound().build();
            }
            // Return the map, which Spring Boot will convert to JSON!
            return ResponseEntity.ok(serviceInfo);
        } catch (AuthException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e){
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{serviceId}/star")
    public ResponseEntity<?> unstarService(@PathVariable("serviceId") int serviceId,
                @CookieValue(value = "jwt_token", required = false) String userCookie) {
        try {
            int userId = checkCookie(userCookie);

            Map<String, Object> serviceInfo = serviceManager.removeStar(serviceId,userId);

            if(serviceInfo == null) {
                return ResponseEntity.notFound().build();
            }
            // Return the map, which Spring Boot will convert to JSON!
            return ResponseEntity.ok(serviceInfo);
        } catch (AuthException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e){
                return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("profile/{serviceId}")
    public ResponseEntity<?> getServiceByProfileId(@PathVariable("serviceId") int serviceId) {

        System.out.println("Getting service by profile id");
        Map<String, Object> serviceInfo = serviceManager.getServiceImagePath(serviceId);

        System.out.println(serviceInfo);
        if(serviceInfo == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the map, which Spring Boot will convert to JSON!
        return ResponseEntity.ok(serviceInfo);
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getServiceStatistics() {

        return ResponseEntity.ok(Collections.singletonMap("star", 1));
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

}