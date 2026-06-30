package com.finalproject.backend.login_register.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailSender {

    private final JavaMailSender mailSender;
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify your Registration");
        message.setText("Thank you for registering! Your 6-digit verification code is: " + code);
        mailSender.send(message);
    }
}
