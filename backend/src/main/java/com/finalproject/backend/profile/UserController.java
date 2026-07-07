package com.finalproject.backend.profile;

import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.DTO.ProfileDTO;
import com.finalproject.backend.services.CookieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserController {
    private final UserService userService;
    private final CookieService cookieService;

    public UserController(UserService userService, CookieService cookieService) {
        this.userService = userService;
        this.cookieService = cookieService;
    }

    @GetMapping("/")
    public ResponseEntity<ProfileDTO> getPersonalProfile(
            @CookieValue(value = "jwt_token") String userCookie
    ) {
        Integer id = cookieService.checkCookie(userCookie);

        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getPublicProfile(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/update")
    public void updateUserProfile(@CookieValue(value = "jwt_token", required = false) String userCookie,
                                  @RequestBody ProfileDTO profileDTO
                                  ) {

        Integer id = cookieService.checkCookie(userCookie);
        if(!profileDTO.getId().equals(id)) {
            throw new RuntimeException("Cookie Mismatch");
        }
        System.out.println(profileDTO.getAboutMe());
        userService.UpdatePublicUser(profileDTO);
    }

}