package com.gencpulse.commit.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gencpulse.commit.dto.ApiResponse;
import com.gencpulse.commit.dto.EmployeeResponse;

@FeignClient(
        name = "EmployeeService",
        contextId = "employeeCommitFeignClient"
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
