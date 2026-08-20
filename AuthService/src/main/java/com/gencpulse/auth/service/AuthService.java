package com.gencpulse.auth.service;

import com.gencpulse.auth.dto.LoginRequest;
import com.gencpulse.auth.dto.LoginResponse;
import com.gencpulse.auth.dto.RegisterRequest;

public interface AuthService {

    String register(
            RegisterRequest request);

    LoginResponse login(
            LoginRequest request);
}