package com.finalproject.backend.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes the weekly calendar slots to the React frontend.
 *
 * Endpoint:
 *   GET /api/slots  →  List<BookingSlotDTO>
 *
 * @CrossOrigin allows the Vite dev server (port 5173) to call this endpoint
 * without being blocked by the browser's same-origin policy.
 */
@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "http://localhost:5173")
public class AvailabilityController {

    private final BookingService bookingService;

    public AvailabilityController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Returns all booking slots for the current week, sorted chronologically.
     *
     * Response JSON example:
     * [
     *   { "id": 1, "slotDateTime": "2026-06-15T09:00:00", "status": "FREE" },
     *   { "id": 2, "slotDateTime": "2026-06-15T10:00:00", "status": "BOOKED" },
     *   ...
     * ]
     */
    @GetMapping
    public ResponseEntity<List<BookingSlotDTO>> getWeeklySlots() {
        return ResponseEntity.ok(bookingService.getAllSlotsForCurrentWeek());
    }
}
