package com.finalproject.backend.service.controllers;

import com.finalproject.backend.repositories.ServiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.finalproject.backend.entities.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ServiceController {

    private final ServiceRepository serviceRepository;

    // Constructor injection matching your AuthController style
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Maps to GET http://localhost:8080/api/services/{id}
    @GetMapping("/{serviceId}")
    public ResponseEntity<?> getServiceById(@PathVariable("serviceId") int serviceId) {
        // 1. Fetch from the database
        Optional<Service> serviceOptional = serviceRepository.findById(serviceId);

        // 2. Handle the case where the service isn't found (returns a 404)
        if (serviceOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 3. Get the actual Service object
        Service service = serviceOptional.get();

        // 4. Create a custom JSON response with only the fields you requested
        // Note: The method names (getId, getUserId, getPhoto) are guesses.
        // You will need to use the actual getter methods defined in your friend's Service entity.
        Map<String, Object> responseJson = new HashMap<>();

        // You mentioned "server id", but based on the URL, it's likely the "serviceId"
        responseJson.put("serviceId", service.getId());

        // Assuming your Service entity has a relationship to User and a photo field
        responseJson.put("serviceBio", service.getBio());
        responseJson.put("serviceImage", service.getImagePath());

        // Return the map, which Spring Boot will convert to JSON!
        return ResponseEntity.ok(responseJson);
    }

    /*
    @GetMapping
    public ResponseEntity<?> getAllServices() {
        //return ResponseEntity.ok(serviceRepository.findAll());
        return ResponseEntity.ok(Collections.singletonMap("anyKey", "anyValue"));
    }
     */
    //@GetMapping("/")


}