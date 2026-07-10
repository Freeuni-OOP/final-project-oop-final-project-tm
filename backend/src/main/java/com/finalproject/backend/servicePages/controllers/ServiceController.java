package com.finalproject.backend.servicePages.controllers;

import com.finalproject.backend.servicePages.commons.CookieChecker;
import com.finalproject.backend.servicePages.logic.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ServiceController {

    // 1. Declare the ServiceManager as a private final variable
    private final ServiceManager serviceManager;
    private final CookieChecker cookieChecker;

    // 2. Inject it via the constructor!
    public ServiceController(ServiceManager serviceManager,
                             CookieChecker cookieChecker) {
        this.serviceManager = serviceManager;
        this.cookieChecker = cookieChecker;
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
        try{
            int userId = cookieChecker.checkCookie(userCookie);
            System.out.println("User ID: " + userId + " Star add controller recieved");
            serviceManager.addStar(serviceId,userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{serviceId}/star")
    public ResponseEntity<?> unstarService(@PathVariable("serviceId") int serviceId
    , @CookieValue(value = "jwt_token", required = false) String userCookie) {
        try{
            int userId = cookieChecker.checkCookie(userCookie);
            System.out.println("User ID: " + userId + " Unstar controller recieved");
            serviceManager.removeStar(serviceId,userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{serviceId}/star")
    public ResponseEntity<?> staredService(@PathVariable("serviceId") int serviceId,
        @CookieValue(value = "jwt_token", required = false) String userCookie) {
        try {
            int userId = cookieChecker.checkCookie(userCookie);
            Map<String, Boolean> stared = serviceManager.getStarEssence(serviceId, userId);
            if (stared == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(stared);
        } catch (Exception e) {
            // Star button must not work and it should defaule false
            return ResponseEntity.ok(Map.of("stared", false));
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

}