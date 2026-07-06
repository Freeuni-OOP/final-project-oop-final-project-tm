package com.finalproject.backend.service.controllers;

import com.finalproject.backend.service.logic.ServiceManager;
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

    // 2. Inject it via the constructor!
    public ServiceController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
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
    public ResponseEntity<?> starService(@PathVariable("serviceId") int serviceId) {

        return ResponseEntity.ok(Collections.singletonMap("anyKey", "anyValue"));
    }

    @DeleteMapping("/{serviceId}/star")
    public ResponseEntity<?> unstarService(@PathVariable("serviceId") int serviceId) {
        return ResponseEntity.ok(Collections.singletonMap("anyKey", "anyValue"));
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