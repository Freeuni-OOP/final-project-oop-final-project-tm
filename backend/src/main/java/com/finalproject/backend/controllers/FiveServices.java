package com.finalproject.backend.controllers;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.repositories.ServiceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/miniServices")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
//this whole thing is so the first 5 services can be displayed on the main page
public class FiveServices {

    private final ServiceRepository serviceRepository;

    public FiveServices(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("/suggested")
    public ResponseEntity<List<Service>> getSuggestedServices() {
        List<Service> services = serviceRepository.findAll(PageRequest.of(0, 5)).getContent();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable int id) {
        Optional<Service> service = serviceRepository.findById(id);
        return service.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


