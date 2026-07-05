package com.finalproject.backend.profile;

import com.finalproject.backend.login_register.config.TokenCreator;
import jakarta.persistence.criteria.CriteriaBuilder;
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
        return ResponseEntity.ok(count);
    }

    @PostMapping("/new/{id}")
    public ResponseEntity<Integer> newFollow(@CookieValue(value = "jwt_token") String userCookie,
                          @PathVariable Integer id) {

        Integer followerId =  checkCookie(userCookie);
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

        Integer followerId = checkCookie(userCookie);

        followService.unfollow(followerId, id);

        Integer updatedCount = new Integer(followService.getFollowers(id));
        return ResponseEntity.ok(updatedCount);

    }

    @GetMapping("/isfollowing/{id}")
    public ResponseEntity<Boolean> isFollowingViewer(@CookieValue(value = "jwt_token") String userCookie,
                                                     @PathVariable Integer id) {
        Integer viewId = checkCookie(userCookie);
        System.out.println("Checking is Following \n");
        Boolean isFollowing = followService.isAFollower(viewId, id);
        return ResponseEntity.ok(isFollowing);
    }

    private Integer checkCookie(String userCookie) {
        if(userCookie == null || userCookie.isEmpty()) {
            throw new RuntimeException("Cookie Missing");
        }

        String mail = tokenCreator.validateTokenAndGetEmail(userCookie);
        return userService.getIdByEmail(mail);
    }

}
