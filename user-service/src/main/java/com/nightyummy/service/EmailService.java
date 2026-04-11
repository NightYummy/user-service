package com.nightyummy.service;

import com.nightyummy.dto.UserEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendUserEventEmail(UserEvent event) {
        String subject = switch (event.operation()) {
            case CREATE -> "Создание аккаунта";
            case DELETE -> "Удаление акаунта";
        };
        String text = switch (event.operation()) {
            case CREATE -> "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
            case DELETE -> "Здравствуйте! Ваш аккаунт был удален.";
        };

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@noreply.com");
        message.setTo(event.email());
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
