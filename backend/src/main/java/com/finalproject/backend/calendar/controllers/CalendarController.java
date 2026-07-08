package com.finalproject.backend.calendar.controllers;

import com.finalproject.backend.calendar.dtos.WeekCalendarDTO;
import com.finalproject.backend.calendar.services.CalendarService;
import com.finalproject.backend.services.CookieService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CalendarController {

    private final CalendarService calendarService;
    private final CookieService cookieService;

    public CalendarController(CalendarService calendarService, CookieService cookieService) {
        this.calendarService = calendarService;
        this.cookieService = cookieService;
    }

    //returns null instead of throwing for a missing, invalid or expired token
    private Integer resolveUserId(String userCookie) {
        if (userCookie == null || userCookie.isEmpty()) return null;
        try {
            return cookieService.checkCookie(userCookie);
        } catch (Exception e) {
            return null;
        }
    }

    //maps Optional.empty() (service doesn't exist) to 404
    //this is what lets WeeklyCalendar.jsx render PageNotFound for a bad serviceId
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<WeekCalendarDTO> getServiceWeek(
            @PathVariable Integer serviceId,
            @RequestParam(defaultValue = "0") int weekOffset) {
        return calendarService.getServiceWeek(serviceId, weekOffset)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //public by design: only reveals which user id owns a service, mirrors getServiceWeek's 404 for a missing one
    //ServiceCalendarPage uses this to decide between the settings and booking calendars
    @GetMapping("/service/{serviceId}/owner")
    public ResponseEntity<Map<String, Integer>> getServiceOwner(@PathVariable Integer serviceId) {
        return calendarService.getServiceOwner(serviceId)
                .map(ownerId -> ResponseEntity.ok(Map.of("ownerId", ownerId)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //private calendar: a user may only view their own, since it exposes reservations they made elsewhere
    //401 when the jwt_token cookie is missing or invalid, 403 when it resolves to a different user
    @GetMapping("/user/{userId}")
    public ResponseEntity<WeekCalendarDTO> getUserWeek(
            @CookieValue(value = "jwt_token", required = false) String userCookie,
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int weekOffset) {
        Integer callerId = resolveUserId(userCookie);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!callerId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(calendarService.getUserWeek(userId, weekOffset));
    }
}
