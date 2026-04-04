package com.nightyummy.controller;

import com.nightyummy.dto.UserEvent;
import com.nightyummy.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notifications", description = "Users management")
@RestController
@RequestMapping("/api/notify")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "Sends an E-mail notification to the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "E-mail sent"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendNotification(@Valid @RequestBody UserEvent event) {
        emailService.sendUserEventEmail(event);
    }
}
