package com.finalproject.backend.profile;

import com.finalproject.backend.constants.NotificationMessages;
import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.services.CookieService;
import com.finalproject.backend.services.NotificationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile/follow")
public class FollowController {
    private final CookieService cookieService;
    private final FollowService followService;
    private final NotificationService notificationService;
    private final UserService userService;
    public FollowController(CookieService cookieService, FollowService followService, NotificationService notificationService, UserService userService) {
        this.cookieService = cookieService;
        this.followService = followService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    // returns follower count
    @GetMapping("/follower/count/{id}")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable Integer id) {
        Integer count = new Integer(followService.getFollowers(id));
        System.out.println(count + '\n');
        return ResponseEntity.ok(count);
    }

    // returns new follower count after viewer follows
    @PostMapping("/new/{id}")
    public ResponseEntity<Integer> newFollow(@CookieValue(value = "jwt_token", required = false) String userCookie,
                          @PathVariable Integer id) {

        Integer followerId =  cookieService.checkCookie(userCookie);
        System.out.println("HERE:  " + followerId);
        followService.follow(followerId, id);


        Integer updatedCount = new Integer(followService.getFollowers(id));
        notificationService.addNotification(id, (NotificationMessages.newFollower + getName(followerId)));
        notificationService.addNotification(followerId, (NotificationMessages.newFollowing + getName(id)));
        return ResponseEntity.ok(updatedCount);
    }

    // returns following count
    @GetMapping("/following/count/{id}")
    public ResponseEntity<Integer> getFollowingCount(@PathVariable Integer id) {
        Integer count = new Integer(followService.getFollowings(id));
        System.out.println(count + '\n');
        return ResponseEntity.ok(count);
    }

    // returns new number of followers after viewer unfollows
    @PostMapping("/delete/{id}")
    public ResponseEntity<Integer> loseFollow(@CookieValue(value = "jwt_token") String userCookie,
                           @PathVariable Integer id) {

        Integer followerId = cookieService.checkCookie(userCookie);

        followService.unfollow(followerId, id);

        Integer updatedCount = new Integer(followService.getFollowers(id));

        notificationService.addNotification(id, (NotificationMessages.gotUnfollowed + getName(followerId)));
        notificationService.addNotification(followerId, (NotificationMessages.unfollow + getName(id)));

        return ResponseEntity.ok(updatedCount);

    }

    // returns boolean whether viewer is following
    @GetMapping("/isfollowing/{id}")
    public ResponseEntity<Boolean> isFollowingViewer(@CookieValue(value = "jwt_token") String userCookie,
                                                     @PathVariable Integer id) {
        Integer viewId = cookieService.checkCookie(userCookie);
        System.out.println("Checking is Following \n");
        Boolean isFollowing = followService.isAFollower(viewId, id);
        return ResponseEntity.ok(isFollowing);
    }

    private String getName(Integer id) {
        return userService.getName(id);
    }

}
