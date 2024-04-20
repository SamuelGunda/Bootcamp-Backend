package com.kasv.gunda.bootcamp.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendNewPassword(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom("samuel.gunda.sg@gmail.com");
        message.setSubject("Your new password");
        message.setText("Your new password is: " + newPassword);

        emailSender.send(message);
    }

    @Async
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom("samuel.gunda.sg@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);

    }
}
