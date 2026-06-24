package com.finalproject.backend.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam Integer serviceId,
            @RequestParam(defaultValue = "0") int weekOffset) {
        return ResponseEntity.ok(bookingService.getAllSlotsForWeek(serviceId, weekOffset));
    }
}
