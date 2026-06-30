package com.finalproject.backend.profile;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class PictureController {
    private PictureService pictureService;
    private UserService userService;
    public PictureController(PictureService pictureService, UserService userService) {
        this.pictureService = pictureService;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("picUrl") MultipartFile file,
                                              @CookieValue(value = "userId", required = false) Integer userIdCookie) {
        String savedFile = pictureService.saveImage(file);
        userService.updateProfilePicture(userIdCookie, savedFile);

        return ResponseEntity.ok(savedFile);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String fileName) {
        System.out.println("before: " + fileName);
        Resource file = pictureService.loadImage(fileName);
        System.out.println("after");
        String contentType = fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                ? "image/jpeg" : "image/png";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }

}
