package com.finalproject.backend.servicefeature.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173") // Allow React to call this
public class GetServiceImageController {

    // Tell Java exactly where the folder is relative to the backend root directory
    // Dao class here GetImageLocation
    private final String UPLOAD_DIRECTORY = "uploads/profiles/";

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userId) {
        try {
            // Reconstruct the file path based on the user ID (assuming they are saved as .jpg)
            // Example: uploads/profiles/123.jpg
            Path imagePath = Paths.get(UPLOAD_DIRECTORY + userId + ".jpg");

            // Check if the file actually exists in the folder
            if (!Files.exists(imagePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Returns a 404
            }

            // Read the physical file from your hard drive into bytes
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Send it back to React
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                    .body(imageBytes);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}