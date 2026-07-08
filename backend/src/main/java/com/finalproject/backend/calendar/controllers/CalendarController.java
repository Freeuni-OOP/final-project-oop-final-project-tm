package com.finalproject.backend.calendar.controllers;

import com.finalproject.backend.calendar.dtos.WeekCalendarDTO;
import com.finalproject.backend.calendar.services.CalendarService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
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

    //no auth/ownership check - any caller can view any user's calendar by id
    //now that this includes the user's own reservations too, it leaks what services they've booked elsewhere
    @GetMapping("/user/{userId}")
    public ResponseEntity<WeekCalendarDTO> getUserWeek(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int weekOffset) {
        return ResponseEntity.ok(calendarService.getUserWeek(userId, weekOffset));
    }
}
