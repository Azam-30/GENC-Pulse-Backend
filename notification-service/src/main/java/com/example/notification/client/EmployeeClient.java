package com.example.notification.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "EMPLOYEESERVICE")
public interface EmployeeClient {
    @GetMapping("/api/employees")
    Map<String, Object> getEmployees();

    @GetMapping("/api/employees/{id}")
    Map<String, Object> getEmployee(@PathVariable("id") Long id);
}
