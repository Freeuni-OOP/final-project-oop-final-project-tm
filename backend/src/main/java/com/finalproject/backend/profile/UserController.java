package com.finalproject.backend.profile;

import com.finalproject.backend.profile.DTO.ProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<ProfileDTO> getPersonalProfile(
            @CookieValue(value = "userId") Integer userIdCookie
    ) {
        return ResponseEntity.ok(userService.getUser(userIdCookie));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getPublicProfile(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/update")
    public void updateUserProfile(@CookieValue(value = "userId", required = false) Integer userIdCookie,
                                  @RequestBody ProfileDTO profileDTO
                                  ) {
        System.out.println("In Controller");
        if(!profileDTO.getId().equals(userIdCookie)) {
            throw new Error("Cookie Mismatch");
        }
        System.out.println(profileDTO.getAboutMe());
        userService.UpdatePublicUser(profileDTO);
    }

}