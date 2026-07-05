package com.finalproject.backend.profile;

import com.finalproject.backend.login_register.config.TokenCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile/follow")
public class FollowController {
    private final TokenCreator tokenCreator;
    private final UserService userService;
    private final FollowService followService;

    public FollowController(TokenCreator tokenCreator, UserService userService, FollowService followService) {
        this.tokenCreator = tokenCreator;
        this.userService = userService;
        this.followService = followService;
    }

    @GetMapping("/follower/count/{id}")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable Integer id) {
        Integer count = new Integer(followService.getFollowers(id));
        System.out.println(count + '\n');
        System.out.println("ndhgiadhgiudrg  dgfd dgfg vf ");
        return ResponseEntity.ok(count);
    }

    @PostMapping("/new/{id}")
    public ResponseEntity<Integer> newFollow(@CookieValue(value = "jwt_token") String userCookie,
                          @PathVariable Integer id) {
        System.out.println("In CONTROLLER");
        if(userCookie == null || userCookie.isEmpty()) {
            throw new RuntimeException("Cookie Missing");
        }

        String mail = tokenCreator.validateTokenAndGetEmail(userCookie);
        System.out.println("HERE:  " + mail);
        Integer followerId = userService.getIdByEmail(mail);
        System.out.println("HERE:  " + followerId);
        followService.follow(followerId, id);

        Integer updatedCount = new Integer(followService.getFollowers(id));
        return ResponseEntity.ok(updatedCount);
    }

    @GetMapping("/following/count/{id}")
    public ResponseEntity<Integer> getFollowingCount(@PathVariable Integer id) {
        Integer count = new Integer(followService.getFollowings(id));
        System.out.println(count + '\n');
        return ResponseEntity.ok(count);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Integer> loseFollow(@CookieValue(value = "jwt_token") String userCookie,
                           @PathVariable Integer id) {
        if(userCookie == null || userCookie.isEmpty()) {
            throw new RuntimeException("Cookie Missing");
        }

        String mail = tokenCreator.validateTokenAndGetEmail(userCookie);
        Integer followerId = userService.getIdByEmail(mail);

        followService.unfollow(followerId, id);

        Integer updatedCount = new Integer(followService.getFollowers(id));
        return ResponseEntity.ok(updatedCount);

    }

}
