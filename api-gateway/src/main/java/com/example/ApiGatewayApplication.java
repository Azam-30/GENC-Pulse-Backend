package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
    
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("employee-service", r -> r.path("/api/employees/**")
                .uri("http://localhost:8081"))
            .route("auth-service", r -> r.path("/api/auth/**")
                .uri("http://localhost:8081"))
            .route("progress-service", r -> r.path("/api/status/**")
                .uri("http://localhost:8082"))
            .route("commit-service", r -> r.path("/api/commits/**")
                .uri("http://localhost:8083"))
            .route("notification-service", r -> r.path("/api/notifications/**")
                .uri("http://localhost:8084"))
            .build();
    }
}
