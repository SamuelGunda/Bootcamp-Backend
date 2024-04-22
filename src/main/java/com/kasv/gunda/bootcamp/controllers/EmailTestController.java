package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.utilities.EmailFunctions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

        private final EmailFunctions emailFunctions;

        public EmailTestController(EmailFunctions emailFunctions) {
            this.emailFunctions = emailFunctions;
        }

        @RequestMapping("/sendEmail")
        public String sendEmail() {
            emailFunctions.sendEmail("samuel.Gunda.sg@gmail.com", "Test Email", "This is a test email");
            return "Email sent";
        }
}
