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
    //generates a random confirmation code that has 6 digits
    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    //sends an email using the given subject and input
    public void sendEmail(String toEmail, String code, String subject, String input) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(input+ code);
        mailSender.send(message);
    }
}
