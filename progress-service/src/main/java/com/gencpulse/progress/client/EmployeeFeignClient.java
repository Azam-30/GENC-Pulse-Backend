package com.gencpulse.progress.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "EmployeeService",
        contextId = "employeeFeignClient"
)
public interface EmployeeFeignClient {

    @GetMapping("/api/employees/{id}")
    Object getEmployeeById(
            @PathVariable Long id);
}
