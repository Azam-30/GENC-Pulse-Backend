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

    private String role;

    private String managerName;

    private String projectName;

    private Boolean active;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}