package com.nightyummy.service;

import com.nightyummy.dto.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-event", groupId = "notification-group")
    public void consume(UserEvent event) {
        emailService.sendUserEventEmail(event);
    }
}
