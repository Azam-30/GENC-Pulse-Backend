package com.gencpulse.employee.controller;

import com.gencpulse.employee.dto.EmployeeRequest;
import com.gencpulse.employee.dto.EmployeeResponse;
import com.gencpulse.employee.response.ApiResponse;
import com.gencpulse.employee.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>>
    createEmployee(
            @RequestBody EmployeeRequest request) {

        EmployeeResponse response =
                employeeService.createEmployee(
                        request);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponse>builder()
                        .status("SUCCESS")
                        .message(
                           "Employee Created")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>>
    getAllEmployees() {

        List<EmployeeResponse> response =
                employeeService.getAllEmployees();

        return ResponseEntity.ok(
                ApiResponse.<List<EmployeeResponse>>
                        builder()
                        .status("SUCCESS")
                        .message("Employees Fetched")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>>
    getById(
            @PathVariable Long id) {

        EmployeeResponse response =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponse>
                        builder()
                        .status("SUCCESS")
                        .message("Employee Found")
                        .data(response)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>>
    updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequest request) {

        EmployeeResponse response =
                employeeService.updateEmployee(
                        id,
                        request);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponse>
                        builder()
                        .status("SUCCESS")
                        .message("Employee Updated")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>>
    deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status("SUCCESS")
                        .message("Employee Deleted")
                        .data("Deleted")
                        .build());
    }
}