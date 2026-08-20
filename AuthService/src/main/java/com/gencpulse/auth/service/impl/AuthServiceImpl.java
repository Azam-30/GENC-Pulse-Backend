package com.gencpulse.auth.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gencpulse.auth.dto.LoginRequest;
import com.gencpulse.auth.dto.LoginResponse;
import com.gencpulse.auth.dto.RegisterRequest;
import com.gencpulse.auth.entity.User;
import com.gencpulse.auth.repository.UserRepository;
import com.gencpulse.auth.service.AuthService;
import com.gencpulse.auth.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl
        implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Override
    public String register(
            RegisterRequest request) {

        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new RuntimeException(
                    "Username already exists");
        }

        User user = User.builder()
                .username(
                        request.getUsername())
                .email(
                        request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .role(
                        request.getRole())
                .active(true)
                .build();

        userRepository.save(user);

        return "User Registered Successfully";
    }

    @Override
    public LoginResponse login(
            LoginRequest request) {

        User user =
                userRepository
                        .findByUsername(
                                request.getUsername())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User Not Found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid Credentials");
        }

        String token =
                jwtUtil.generateToken(
                        user.getUsername(),
                        user.getRole());

        return LoginResponse.builder()
                .token(token)
                .username(
                        user.getUsername())
                .role(
                        user.getRole().name())
                .build();
    }
}