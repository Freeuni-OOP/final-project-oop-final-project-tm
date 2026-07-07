package com.finalproject.backend.calendar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String appBaseUrl;

    public EmailNotificationService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //mailSender can be null when no mail config is present; logs and no-ops instead of failing
    //send failures are swallowed so a broken mail server never breaks the booking flow
    public void sendBookingRequestNotification(
            String ownerEmail,
            Integer slotId,
            Integer takerId,
            LocalDateTime start,
            LocalDateTime end,
            String clientName,
            String clientEmail) {

        if (mailSender == null) {
            System.out.println("[EmailService] Mail sender not configured - notification skipped.");
            return;
        }

        if (ownerEmail == null || ownerEmail.isBlank()) {
            System.out.println("[EmailService] No owner email resolved - notification skipped.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(ownerEmail);
            helper.setSubject("New Booking Request - " + formatRange(start, end));
            helper.setText(buildHtmlBody(slotId, takerId, start, end, clientName, clientEmail), true);

            mailSender.send(message);
            System.out.println("[EmailService] Notification sent to " + ownerEmail);

        } catch (Exception e) {
            System.err.println("[EmailService] Failed to send notification: " + e.getMessage());
        }
    }

    //formats using only start's date; assumes start and end fall on the same calendar day
    //a booking crossing midnight would print a misleading range
    private String formatRange(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter day = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm a");
        return start.format(day) + " " + start.format(time) + " to " + end.format(time);
    }

    //confirm/reject are plain GET links with no auth token
    //merely fetching the URL (e.g. a mail client's link-preview/scanner) mutates booking state
    //clientName/clientEmail are escaped to prevent HTML injection; the links aren't since they're server-built ids only
    private String buildHtmlBody(
            Integer slotId,
            Integer takerId,
            LocalDateTime start,
            LocalDateTime end,
            String clientName,
            String clientEmail) {

        String confirmUrl = appBaseUrl + "/api/bookings/confirm/" + slotId + "/" + takerId;
        String rejectUrl  = appBaseUrl + "/api/bookings/reject/"  + slotId + "/" + takerId;

        return "<!DOCTYPE html>"
            + "<html><head><meta charset='UTF-8'>"
            + "<style>"
            + "  body{font-family:'Segoe UI',Arial,sans-serif;background:#f0f4f8;margin:0;padding:30px;}"
            + "  .card{max-width:580px;margin:auto;background:#fff;border-radius:12px;"
            + "         padding:36px;box-shadow:0 6px 24px rgba(0,0,0,.10);}"
            + "  h2{color:#2c7be5;margin:0 0 8px;}"
            + "  .divider{border:none;border-top:1px solid #e8eaf0;margin:20px 0;}"
            + "  .label{color:#888;font-size:.82rem;text-transform:uppercase;letter-spacing:.05em;}"
            + "  .value{color:#1a1a2e;font-size:1rem;font-weight:600;margin:2px 0 14px;}"
            + "  .btn{display:inline-block;padding:13px 28px;border-radius:8px;text-decoration:none;"
            + "       font-weight:700;font-size:.9rem;margin-right:10px;margin-top:8px;}"
            + "  .btn-confirm{background:#28a745;color:#fff;}"
            + "  .btn-reject {background:#dc3545;color:#fff;}"
            + "  .footer{margin-top:28px;color:#aaa;font-size:.75rem;text-align:center;}"
            + "</style></head><body>"
            + "<div class='card'>"
            + "  <h2>New Booking Request</h2>"
            + "  <p style='color:#555;margin:0 0 20px;'>A client has requested an appointment. "
            + "     Please review the details and take action.</p>"
            + "  <hr class='divider'>"
            + "  <p class='label'>Client Name</p>"
            + "  <p class='value'>" + escapeHtml(clientName) + "</p>"
            + "  <p class='label'>Client Email</p>"
            + "  <p class='value'><a href='mailto:" + escapeHtml(clientEmail) + "'>"
            +      escapeHtml(clientEmail) + "</a></p>"
            + "  <p class='label'>Requested Time</p>"
            + "  <p class='value'>" + formatRange(start, end) + "</p>"
            + "  <hr class='divider'>"
            + "  <p style='color:#555;'>Click an action below to update the booking:</p>"
            + "  <a href='" + confirmUrl + "' class='btn btn-confirm'>Confirm Booking</a>"
            + "  <a href='" + rejectUrl  + "' class='btn btn-reject' >Reject Booking</a>"
            + "  <p class='footer'>Sent automatically by the Service Clinic Booking System.</p>"
            + "</div>"
            + "</body></html>";
    }

    //escapes &, <, >, ", and '
    //clientEmail is interpolated into a single-quoted href='mailto:...' attribute above
    //so single quotes must be escaped too, or a value could break out and inject markup
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
