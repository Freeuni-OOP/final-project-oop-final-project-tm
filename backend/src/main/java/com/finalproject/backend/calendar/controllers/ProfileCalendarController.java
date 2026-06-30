package com.finalproject.backend.calendar.controllers;

import com.finalproject.backend.calendar.dtos.WeeklyCalendarDTO;
import com.finalproject.backend.calendar.services.CalendarService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileCalendarController {

    private final CalendarService calendarService;

    public ProfileCalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WeeklyCalendarDTO> getUserWeeklyCalendar(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int weekOffset) {
        return ResponseEntity.ok(calendarService.getWeeklyCalendarForUser(userId, weekOffset));
    }
}
