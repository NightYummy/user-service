package com.nightyummy.service;

import com.nightyummy.dto.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private static final String TOPIC = "user-event";

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UserEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
