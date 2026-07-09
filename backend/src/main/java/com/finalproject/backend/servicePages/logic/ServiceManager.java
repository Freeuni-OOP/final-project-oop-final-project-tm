package com.finalproject.backend.servicePages.logic;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.StarID;
import com.finalproject.backend.entities.Stars;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.profile.UserService;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.StarRepository;
import com.finalproject.backend.repositories.UserRepository;
import com.finalproject.backend.servicePages.exception.AuthException;
import org.antlr.v4.runtime.Token;
import org.springframework.transaction.annotation.Transactional;
import com.finalproject.backend.login_register.config.TokenCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceManager {

    private final ServiceRepository serviceRepository;
    private final StarRepository starRepository;
    private final TokenCreator tokenCreator;
    private final UserService userService;
    private final UserRepository userRepository;

    // Constructor injection matching your AuthController style
    public ServiceManager(ServiceRepository serviceRepository,
                          StarRepository starRepository,
                          TokenCreator tokenCreator,
                          UserService userService,
                          UserRepository userRepository) {
        this.starRepository = starRepository;
        this.serviceRepository = serviceRepository;
        this.tokenCreator = tokenCreator;
        this.userService = userService;
        this.userRepository = userRepository;
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

    @Transactional
    public Map<String, Object> addStar(int serviceId, int userId) {

        // 1. Check if already starred
        if (!starRepository.existsByUserIdAndServiceId(userId, serviceId)) {
            throw new RuntimeException("service already starred");
        }

        // 2. Fetch safely (Throws an exception and stops execution if ID is completely invalid)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // 3. Set up and save the relationship
        StarID id = new StarID(userId, serviceId);
        Stars con = new Stars();
        con.setStarID(id);
        con.setStarred(service);
        con.setStarer(user);

        starRepository.save(con);

        // 4. Increment the counter
        int x = serviceRepository.addStar(serviceId);

        // 5. If the update failed for some reason, throw an error to trigger a ROLLBACK
        if (x == 0) {
            throw new RuntimeException("Failed to update service star count");
        }

        return Map.of("situation", "ok");
    }

    @Transactional
    public Map<String, Object> removeStar(int serviceId, int userId) {

        // 1. Check if the star actually exists (Notice the '!' at the beginning)
        if (!starRepository.existsByUserIdAndServiceId(userId, serviceId)) {
            throw new RuntimeException("Cannot unstar: Service is not starred by this user");
        }

        // 2. Delete by ID directly (No need to fetch User or Service!)
        StarID id = new StarID(userId, serviceId);
        starRepository.deleteById(id);

        // 3. Decrement the counter
        int x = serviceRepository.removeStar(serviceId);

        // 4. If the update failed, rollback
        if (x == 0) {
            throw new RuntimeException("Failed to update service star count");
        }

        return Map.of("situation", "ok");
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
