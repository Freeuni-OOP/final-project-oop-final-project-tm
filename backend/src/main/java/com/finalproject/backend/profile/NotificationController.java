package com.finalproject.backend.profile;

import com.finalproject.backend.profile.DTO.NotificationDTO;
import com.finalproject.backend.services.CookieService;
import com.finalproject.backend.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/profile/notification")
public class NotificationController {
    private CookieService cookieService;
    private NotificationService notificationService;

    public NotificationController(CookieService cookieService, NotificationService notificationService) {
        this.cookieService = cookieService;
        this.notificationService = notificationService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@CookieValue(value = "jwt_token") String userCookie) {
        Integer id = cookieService.checkCookie(userCookie);

        return ResponseEntity.ok(notificationService.getNotification(id));
    }

    @PostMapping("/seen")
    public void SeeMessages(@CookieValue(value = "jwt_token") String userCookie) {
        Integer id = cookieService.checkCookie(userCookie);
        notificationService.setSeen(id);
    }
}
