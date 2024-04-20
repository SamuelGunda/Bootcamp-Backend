package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.services.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

        private final EmailService emailService;

        public EmailTestController(EmailService emailService) {
            this.emailService = emailService;
        }

        @RequestMapping("/sendEmail")
        public String sendEmail() {
            emailService.sendEmail("samuel.Gunda.sg@gmail.com", "Test Email", "This is a test email");
            return "Email sent";
        }
}
