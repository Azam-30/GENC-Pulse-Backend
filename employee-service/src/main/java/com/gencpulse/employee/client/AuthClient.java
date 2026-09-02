package com.gencpulse.employee.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gencpulse.employee.dto.RegisterRequestDto;

@FeignClient(name = "AUTHSERVICE")
public interface AuthClient {

    @PostMapping("/api/auth/register")
    Object registerUser(
            @RequestBody
            RegisterRequestDto request);
}