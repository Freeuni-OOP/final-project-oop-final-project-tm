package com.finalproject.backend.profile;


import com.finalproject.backend.login_register.config.TokenCreator;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class PictureController {
    private PictureService pictureService;
    private UserService userService;
    private final TokenCreator tokenCreator;

    public PictureController(PictureService pictureService, UserService userService, TokenCreator tokenCreator) {
        this.pictureService = pictureService;
        this.userService = userService;
        this.tokenCreator = tokenCreator;
    }


    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("picUrl") MultipartFile file,
                                              @CookieValue(value = "jwt_token", required = false) String userCookie) {
        String savedFile = pictureService.saveImage(file);

        if(userCookie == null || userCookie.isEmpty()) {
            throw new Error("Cookie Missing");
        }

        String email = tokenCreator.validateTokenAndGetEmail(userCookie);
        Integer id = userService.getIdByEmail(email);

        userService.updateProfilePicture(id, savedFile);

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
