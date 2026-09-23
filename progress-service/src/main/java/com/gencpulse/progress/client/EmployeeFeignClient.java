package com.gencpulse.progress.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gencpulse.progress.dto.ApiResponse;
import com.gencpulse.progress.dto.EmployeeResponse;



@FeignClient(
        name = "EmployeeService",
        contextId = "employeeFeignClient"
)
public interface EmployeeFeignClient {

    @GetMapping("/api/employees/{id}")
    Object getEmployeeById(
            @PathVariable Long id);
    
    @GetMapping("/api/employees/manager/{managerId}")
    ApiResponse<List<EmployeeResponse>>
    getEmployeesByManagerId(
            @PathVariable Long managerId);
}
