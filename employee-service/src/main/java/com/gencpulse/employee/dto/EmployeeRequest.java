package com.gencpulse.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequest {

    @NotBlank(message = "Employee Code is required")
    private String employeeCode;

    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    private String designation;

    private String technology;

    private String location;

    private String batch;

    private Long managerId;

    private String managerName;

    private String projectName;

    @NotNull(message = "Active status is required")
    private Boolean active;
}