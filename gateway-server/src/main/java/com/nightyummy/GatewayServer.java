package com.nightyummy;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServer {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServer.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/user/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("userServiceCB")
                                .setFallbackUri("forward:/fallback/users")))
                        .uri("lb://user-service"))   // lb:// означает "найти через eureka"
                .build();
    }
}
