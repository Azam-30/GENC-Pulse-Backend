package com.gencpulse.employee.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {

    private String username;

    private String email;

    private String password;

    private Role role;

    private Long employeeId;
}