package com.finalproject.backend.calendar.controllers;

import com.finalproject.backend.calendar.dtos.BookingRequestDTO;
import com.finalproject.backend.calendar.services.BookingService;
import com.finalproject.backend.calendar.services.BookingService.BookingActionResult;
import com.finalproject.backend.calendar.services.BookingService.BookingCreated;
import com.finalproject.backend.calendar.services.EmailNotificationService;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {

    private final BookingService bookingService;
    private final EmailNotificationService emailService;
    private final UserRepository userRepository;

    public BookingController(BookingService bookingService,
                             EmailNotificationService emailService,
                             UserRepository userRepository) {
        this.bookingService = bookingService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    //takerId comes from a raw, unsigned cookie value - not a verified session
    //notification failures are swallowed here so a broken mail step never undoes an already-created booking
    //validation errors become 400; a fully-booked slot (empty result) becomes 409, not the same failure mode
    @PostMapping("/request")
    public ResponseEntity<?> requestBooking(
            @CookieValue(value = "userId", required = false) Integer takerId,
            @RequestBody BookingRequestDTO dto) {

        if (takerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "You must be logged in to book."));
        }

        Optional<BookingCreated> result;
        try {
            result = bookingService.requestBooking(
                    dto.getServiceId(), dto.getDate(), dto.getStartTime(), dto.getEndTime(), takerId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "That time is fully booked. Please pick another."));
        }

        BookingCreated created = result.get();
        try {
            User booker = userRepository.findById(takerId).orElse(null);
            String name = booker != null ? booker.getFirstName() + " " + booker.getLastName() : "Unknown";
            String email = booker != null ? booker.getEmail() : "";
            String ownerEmail = bookingService.getServiceOwnerEmail(created.slotId()).orElse(null);
            emailService.sendBookingRequestNotification(
                    ownerEmail, created.slotId(), takerId, created.start(), created.end(), name, email);
        } catch (Exception e) {
            System.err.println("[BookingController] Notification step failed: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
                "slotId", created.slotId(),
                "status", "PENDING"));
    }

    //GET endpoint that mutates state with no auth token, meant to be opened directly from the email link
    //returns full HTML instead of JSON since a browser opens it directly, not a fetch call
    //"not found" and "already processed" are indistinguishable here since confirmBooking's empty result covers both
    @GetMapping(value = "/confirm/{slotId}/{takerId}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> confirmBooking(@PathVariable Integer slotId, @PathVariable Integer takerId) {
        Optional<BookingActionResult> result = bookingService.confirmBooking(slotId, takerId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .contentType(MediaType.TEXT_HTML)
                    .body(buildPage("Already Processed", "#f0a500",
                            "This request was already handled or does not exist.", null, null));
        }

        BookingActionResult booking = result.get();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(buildPage("Booking Confirmed!", "#28a745",
                        "You have <strong>confirmed</strong> the appointment for:",
                        booking.clientName(), formatSlot(booking.slotDateTime())));
    }

    //same unauthenticated GET-link pattern as confirmBooking, but destructive
    //rejecting deletes the booking row (and the slot too if nothing else references it), not just a status flip
    @GetMapping(value = "/reject/{slotId}/{takerId}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> rejectBooking(@PathVariable Integer slotId, @PathVariable Integer takerId) {
        Optional<BookingActionResult> result = bookingService.rejectBooking(slotId, takerId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .contentType(MediaType.TEXT_HTML)
                    .body(buildPage("Already Processed", "#f0a500",
                            "This request was already handled or does not exist.", null, null));
        }

        BookingActionResult booking = result.get();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(buildPage("Booking Rejected", "#dc3545",
                        "You have <strong>rejected</strong> the appointment request for:",
                        booking.clientName(), formatSlot(booking.slotDateTime())));
    }

    //formats a slot's start time for display on the confirm/reject result pages
    private String formatSlot(LocalDateTime slotDateTime) {
        return slotDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a"));
    }

    //unlike EmailNotificationService.escapeHtml, clientName is interpolated here with no escaping
    //a user-controlled name containing markup would render/execute on this page
    private String buildPage(String heading, String color,
                             String message, String clientName, String slotTime) {
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
