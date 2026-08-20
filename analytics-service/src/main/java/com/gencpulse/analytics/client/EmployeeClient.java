package com.gencpulse.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "EmployeeService",
        contextId = "analyticsEmployeeClient"
)
public interface EmployeeClient {

    @GetMapping("/api/employees")
    Object getAllEmployees();
}
