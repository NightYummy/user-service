package com.nightyummy;

@RestController
public class FallbackController {
    @GetMapping("/fallback/users")
    public Mono<String> usersFallback() {
        return Mono.just("Сервис пользователей временно недоступен. Попробуйте позже.");
    }
}
