package com.nightyummy.controller;

import com.nightyummy.dto.UserEvent;
import com.nightyummy.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendNotification(@Valid @RequestBody UserEvent event) {
        emailService.sendUserEventEmail(event);
    }
}
