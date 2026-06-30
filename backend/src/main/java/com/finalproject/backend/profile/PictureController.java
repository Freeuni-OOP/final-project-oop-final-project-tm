package com.finalproject.backend.profile;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:5182")
@RestController
@RequestMapping("api/images")
public class PictureController {

    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        try {
            Resource file = pictureService.loadImage(filename);

            // EXPLICIT CHECK: If the file is null, the service failed to find it.
            // Throwing an exception here forces the code to jump to your catch block.
            if (file == null) {
                throw new Exception("File not found");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(file);

        } catch (Exception e) {
            // Now this block will actually run when the image is missing!
            Path defaultPath = Paths.get("Profile-pictures/default.png").toAbsolutePath().normalize();

            try {
                Resource defaultImage = new UrlResource(defaultPath.toUri());
                if (defaultImage.exists() && defaultImage.isReadable()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .body(defaultImage);
                }
            } catch (Exception ex) {
                // Handle edge case where even the default image fails
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
