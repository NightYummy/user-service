package com.nightyummy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback/users")
    public Mono<String> usersFallback() {
        return Mono.just("Сервис пользователей временно недоступен. Попробуйте позже.");
    }
}
