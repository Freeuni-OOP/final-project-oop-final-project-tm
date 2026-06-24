package com.finalproject.backend.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

/**
 * Handles all booking-related HTTP endpoints.
 *
 * POST /api/bookings/request      – client submits a booking request
 * GET  /api/bookings/confirm/{id} – clinic owner confirms from email link
 * GET  /api/bookings/reject/{id}  – clinic owner rejects from email link
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService           bookingService;
    private final EmailNotificationService emailService;

    public BookingController(BookingService bookingService,
                             EmailNotificationService emailService) {
        this.bookingService = bookingService;
        this.emailService   = emailService;
    }

    // ── POST /api/bookings/request ────────────────────────────────────────────

    @PostMapping("/request")
    public ResponseEntity<?> requestBooking(@RequestBody BookingRequestDTO dto) {
        Optional<BookingSlotDTO> result = bookingService.requestBooking(dto);

        if (result.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", "This slot is no longer available."));
        }

        BookingSlotDTO updatedSlot = result.get();
        emailService.sendBookingRequestNotification(
            updatedSlot, dto.getClientName(), dto.getClientEmail());

        return ResponseEntity.ok(updatedSlot);
    }

    // ── GET /api/bookings/confirm/{id} ────────────────────────────────────────

    @GetMapping(value = "/confirm/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> confirmBooking(@PathVariable Long id) {
        Optional<BookingSlot> result = bookingService.confirmBooking(id);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.GONE)
                .contentType(MediaType.TEXT_HTML)
                .body(buildPage("⚠️ Already Processed", "#f0a500",
                    "This request was already handled or does not exist.", null, null));
        }

        BookingSlot slot = result.get();

        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(buildPage("✅ Booking Confirmed!", "#28a745",
                "You have <strong>confirmed</strong> the appointment for:",
                slot.getClientName(), formatSlot(slot)));
    }

    // ── GET /api/bookings/reject/{id} ─────────────────────────────────────────

    @GetMapping(value = "/reject/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> rejectBooking(@PathVariable Long id) {
        Optional<BookingSlot> result = bookingService.rejectBooking(id);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.GONE)
                .contentType(MediaType.TEXT_HTML)
                .body(buildPage("⚠️ Already Processed", "#f0a500",
                    "This request was already handled or does not exist.", null, null));
        }

        BookingSlot slot = result.get();

        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(buildPage("❌ Booking Rejected", "#dc3545",
                "You have <strong>rejected</strong> the appointment request for:",
                slot.getClientName(), formatSlot(slot)));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String formatSlot(BookingSlot slot) {
        return slot.getSlotDateTime()
            .format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a"));
    }

    private String buildPage(String title, String color,
                              String message, String clientName, String slotTime) {
        String icon = title.substring(0, title.indexOf(' '));
        String heading = title.substring(title.indexOf(' ') + 1);

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>")
          .append("<meta name='viewport' content='width=device-width,initial-scale=1'>")
          .append("<title>").append(heading).append("</title><style>")
          .append("*{box-sizing:border-box;margin:0;padding:0}")
          .append("body{font-family:'Segoe UI',Arial,sans-serif;background:#f0f4f8;")
          .append("display:flex;align-items:center;justify-content:center;min-height:100vh;padding:20px}")
          .append(".card{background:#fff;border-radius:16px;padding:40px 36px;max-width:480px;")
          .append("width:100%;box-shadow:0 8px 30px rgba(0,0,0,.12);text-align:center}")
          .append(".icon{font-size:3.5rem;margin-bottom:16px}")
          .append("h1{font-size:1.5rem;color:").append(color).append(";margin-bottom:12px}")
          .append("p{color:#555;font-size:.95rem;line-height:1.6}")
          .append(".detail{background:#f7f9fc;border-radius:8px;padding:14px 18px;")
          .append("margin:18px 0;text-align:left}")
          .append(".detail span{display:block;font-size:.78rem;color:#999;text-transform:uppercase;")
          .append("letter-spacing:.05em;margin-bottom:3px}")
          .append(".detail strong{font-size:1rem;color:#1a1a2e}")
          .append(".footer{margin-top:24px;font-size:.75rem;color:#bbb}")
          .append("</style></head><body><div class='card'>")
          .append("<div class='icon'>").append(icon).append("</div>")
          .append("<h1>").append(heading).append("</h1>")
          .append("<p>").append(message).append("</p>");

        if (clientName != null) {
            sb.append("<div class='detail'><span>Client</span>")
              .append("<strong>").append(clientName).append("</strong></div>");
        }
        if (slotTime != null) {
            sb.append("<div class='detail'><span>Appointment</span>")
              .append("<strong>").append(slotTime).append("</strong></div>");
        }

        sb.append("<p class='footer'>Service Clinic Booking System</p>")
          .append("</div></body></html>");
        return sb.toString();
    }
}
