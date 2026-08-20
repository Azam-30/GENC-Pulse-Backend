package com.gencpulse.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gencpulse.auth.dto.LoginRequest;
import com.gencpulse.auth.dto.LoginResponse;
import com.gencpulse.auth.dto.RegisterRequest;
import com.gencpulse.auth.response.ApiResponse;
import com.gencpulse.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>>
    register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status("SUCCESS")
                        .message("Registration Successful")
                        .data(
                                authService.register(
                                        request))
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>>
    login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .status("SUCCESS")
                        .message("Login Successful")
                        .data(
                                authService.login(
                                        request))
                        .build());
    }
}