package com.gencpulse.employee.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;

    private String employeeCode;

    private String name;

    private String email;

    private String username;

    private String role;

    private String designation;

    private String technology;

    private String location;

    private String batch;

    private Long managerId;

    private String managerName;

    private String projectName;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}