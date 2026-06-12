package com.finalproject.backend.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GET /api/slots?weekOffset=0  → current week
 * GET /api/slots?weekOffset=1  → next week
 * GET /api/slots?weekOffset=-1 → last week
 */
@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "http://localhost:5173")
public class AvailabilityController {

    private final BookingService bookingService;

    public AvailabilityController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingSlotDTO>> getWeeklySlots(
            @RequestParam(defaultValue = "0") int weekOffset) {
        return ResponseEntity.ok(bookingService.getAllSlotsForWeek(weekOffset));
    }
}
