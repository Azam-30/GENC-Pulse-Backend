package com.gencpulse.gateway.route;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    private static final List<String> OPEN_API_ENDPOINTS =
            List.of(

                    "/api/auth/register",

                    "/api/auth/login"
            );

    public boolean isSecured(
            String path) {

        return OPEN_API_ENDPOINTS
                .stream()
                .noneMatch(path::contains);
    }
}