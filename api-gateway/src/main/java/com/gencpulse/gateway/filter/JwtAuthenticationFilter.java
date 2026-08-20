package com.gencpulse.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.gencpulse.gateway.route.RoleValidator;
import com.gencpulse.gateway.route.RouteValidator;
import com.gencpulse.gateway.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter
        implements GlobalFilter, Ordered {

    private final RouteValidator routeValidator;

    private final RoleValidator roleValidator;

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(
            RouteValidator routeValidator,
            RoleValidator roleValidator,
            JwtUtil jwtUtil) {

        this.routeValidator = routeValidator;
        this.roleValidator = roleValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        if (routeValidator.isSecured(path)) {

            String authHeader =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst(
                                    HttpHeaders.AUTHORIZATION);

            if (authHeader == null) {

                exchange.getResponse()
                        .setStatusCode(
                                HttpStatus.UNAUTHORIZED);

                return exchange.getResponse()
                        .setComplete();
            }

            String token = authHeader;

            if (token.startsWith("Bearer ")) {

                token = token.substring(7);
            }

            if (!jwtUtil.validateToken(token)) {

                exchange.getResponse()
                        .setStatusCode(
                                HttpStatus.UNAUTHORIZED);

                return exchange.getResponse()
                        .setComplete();
            }

            String role =
                    jwtUtil.extractRole(token);

            if (!roleValidator.hasAccess(
                    path,
                    role)) {

                exchange.getResponse()
                        .setStatusCode(
                                HttpStatus.FORBIDDEN);

                return exchange.getResponse()
                        .setComplete();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {

        return -1;
    }
}