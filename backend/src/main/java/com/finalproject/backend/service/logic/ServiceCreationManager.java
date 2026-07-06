package com.finalproject.backend.service.logic;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.UserRepository;
import com.finalproject.backend.service.model.ServiceCreationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServiceCreationManager {

    private final ServiceRepository serviceRepository;

    // Where uploaded images get stored on disk. Adjust to wherever your
    // static/uploads folder actually lives.
    private static final Path uploadPath = Paths.get("backend/Profile-pictures");

    private final UserRepository userRepository;

    public ServiceCreationManager(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public int postServiceInformation(ServiceCreationRequest request, Integer userId) throws IOException {

        // 1. Handle the image upload first (if one was provided)
        String imagePath = null;
        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            imagePath = saveImage(request.getProfilePicture());
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        User user = userOptional.get();

        // 2. Build the entity from the request
        Service service = new Service();
        service.setProviderId(user);
        service.setTitle(request.getTitle());
        service.setCategory(request.getCategory());
        service.setAddress(request.getPlace());
        service.setBio(request.getBio());
        service.setImagePath(uploadPath + "/" + imagePath);

        // Date parsing logic
        String date = request.getDate();
        Instant instant = Instant.parse(date);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Tbilisi"));

        service.setTimePosted(dateTime);
        service.setMaxCapacity(request.getMaxCapacity());

        // Price arrives as a String from the form — parse it safely
        try {
            service.setPrice(Double.parseDouble(request.getPrice()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format: " + request.getPrice());
        }

        // 3. Persist to the database — save() blocks until the write completes,
        // so by the time this method returns, the row is committed.
        Service savedService = serviceRepository.save(service);

        // 4. Return the generated id so the controller can hand it back to the frontend
        return savedService.getId();
    }

    private String saveImage(org.springframework.web.multipart.MultipartFile file) throws IOException {
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Avoid filename collisions by prefixing with a random UUID
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String storedFilename = UUID.randomUUID() + extension;

        Path targetPath = uploadPath.resolve(storedFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Return whatever path format the rest of your app expects to serve images from
        return storedFilename;
    }
}