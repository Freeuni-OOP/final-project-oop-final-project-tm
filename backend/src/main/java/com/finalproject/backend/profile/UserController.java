package com.finalproject.backend.profile;

import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.DTO.ProfileDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserController {
    private final UserService userService;
    private final TokenCreator tokenCreator;

    public UserController(UserService userService, TokenCreator tokenCreator) {
        this.userService = userService;
        this.tokenCreator = tokenCreator;
    }

    @GetMapping("/")
    public ResponseEntity<ProfileDTO> getPersonalProfile(
            @CookieValue(value = "jwt_token") String userCookie
    ) {

        if(userCookie == null || userCookie.isEmpty()) {
            throw new Error("Cookie Missing");
        }

        String email = tokenCreator.validateTokenAndGetEmail(userCookie);
        Integer id = userService.getIdByEmail(email);

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
        System.out.println("In Controller");
        if(userCookie == null || userCookie.isEmpty()) {
            throw new Error("Cookie Missing");
        }
        String email = tokenCreator.validateTokenAndGetEmail(userCookie);

        Integer id = userService.getIdByEmail(email);
        if(!profileDTO.getId().equals(id)) {
            throw new Error("Cookie Mismatch");
        }
        System.out.println(profileDTO.getAboutMe());
        userService.UpdatePublicUser(profileDTO);
    }

}