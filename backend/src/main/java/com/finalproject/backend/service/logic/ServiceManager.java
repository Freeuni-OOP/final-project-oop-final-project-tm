package com.finalproject.backend.service.logic;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.repositories.ServiceRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceManager {

    private final ServiceRepository serviceRepository;

    // Constructor injection matching your AuthController style
    public ServiceManager(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public  Map<String, Object> getServiceInformation(int serviceId) {
        // 1. Fetch from the database
        Optional<Service> serviceOptional = serviceRepository.findById(serviceId);

        // 2. Handle the case where the service isn't found (returns a 404)
        if (serviceOptional.isEmpty()) {
            return null;
        }

        // 3. Get the actual Service object
        Service service = serviceOptional.get();

        // 4. Create a custom JSON response with only the fields you requested
        Map<String, Object> responseJson = new HashMap<>();

        responseJson.put("serviceId", service.getId());
        responseJson.put("serviceBio", service.getBio());
        responseJson.put("serviceImage", service.getImagePath());
        responseJson.put("serviceTitle", service.getTitle());
        responseJson.put("servicePrice", service.getPrice());
        responseJson.put("serviceCategory", service.getCategory());
        responseJson.put("serviceAddress", service.getAddress());
        responseJson.put("serviceProfileId", service.getProviderId().getId());

        return responseJson;
    }

    public Map<String, Object> getServiceImagePath(int serviceId) {
        // 1. Fetch from the database
        Optional<Service> serviceOptional = serviceRepository.findById(serviceId);

        // 2. Handle the case where the service isn't found (returns a 404)
        if (serviceOptional.isEmpty()) {
            return null;
        }

        // 3. Get the actual Service object
        Service service = serviceOptional.get();

        // 4. Create a custom JSON response with ONLY the image path
        Map<String, Object> responseJson = new HashMap<>();

        // CRITICAL: The key must be "imagePath" to match your React frontend!
        responseJson.put("imagePath", service.getImagePath());

        return responseJson;
    }
}
