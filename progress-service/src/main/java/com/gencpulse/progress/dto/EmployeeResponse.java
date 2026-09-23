package com.gencpulse.progress.dto;

import lombok.Data;

@Data
public class EmployeeResponse {

    private Long id;

    private String employeeCode;

    private String name;

    private String role;

    private Long managerId;

    private String managerName;
}