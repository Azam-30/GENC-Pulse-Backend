package com.gencpulse.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	

	    public GatewayConfig() {
	        System.out.println("GATEWAY CONFIG LOADED");
	    }

	    @Bean
	    public RouteLocator customRouteLocator(
	            RouteLocatorBuilder builder) {

	        System.out.println("ROUTES CREATED");

	        return builder.routes()

	                .route("employee-service",
	                        r -> r.path("/api/employees/**")
	                                .uri("lb://EMPLOYEESERVICE"))

	                .route("progress-service",
	                        r -> r.path("/api/progress/**")
	                                .uri("lb://PROGRESSSERVICE"))

	                .route("commit-service",
	                        r -> r.path("/api/commits/**")
	                                .uri("lb://COMMITSERVICE"))

	                .route("analytics-service",
	                        r -> r.path("/api/analytics/**")
	                                .uri("lb://ANALYTICSSERVICE"))

	                .build();
	    }
	
}