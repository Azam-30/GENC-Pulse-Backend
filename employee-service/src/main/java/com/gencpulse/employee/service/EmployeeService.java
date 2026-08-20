package com.gencpulse.employee.service;

import com.gencpulse.employee.dto.EmployeeRequest;
import com.gencpulse.employee.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request);

    void deleteEmployee(Long id);
}