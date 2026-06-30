package com.finalproject.backend.calendar.services;

import com.finalproject.backend.calendar.dtos.BookingSlotDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String appBaseUrl;

    public EmailNotificationService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingRequestNotification(
            String ownerEmail,
            BookingSlotDTO slot,
            Integer takerId,
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
            helper.setSubject("New Booking Request - " + formatDateTime(slot));
            helper.setText(buildHtmlBody(slot, takerId, clientName, clientEmail), true);

            mailSender.send(message);
            System.out.println("[EmailService] Notification sent to " + ownerEmail);

        } catch (Exception e) {
            System.err.println("[EmailService] Failed to send notification: " + e.getMessage());
        }
    }

    private String formatDateTime(BookingSlotDTO slot) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
        return slot.getSlotDateTime().format(fmt);
    }

    private String buildHtmlBody(
            BookingSlotDTO slot,
            Integer takerId,
            String clientName,
            String clientEmail) {

        String confirmUrl = appBaseUrl + "/api/bookings/confirm/" + slot.getId() + "/" + takerId;
        String rejectUrl  = appBaseUrl + "/api/bookings/reject/"  + slot.getId() + "/" + takerId;

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
            + "  <p class='label'>Requested Slot</p>"
            + "  <p class='value'>" + formatDateTime(slot) + "</p>"
            + "  <hr class='divider'>"
            + "  <p style='color:#555;'>Click an action below to update the booking:</p>"
            + "  <a href='" + confirmUrl + "' class='btn btn-confirm'>Confirm Booking</a>"
            + "  <a href='" + rejectUrl  + "' class='btn btn-reject' >Reject Booking</a>"
            + "  <p class='footer'>Sent automatically by the Service Clinic Booking System.</p>"
            + "</div>"
            + "</body></html>";
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
    }
}
