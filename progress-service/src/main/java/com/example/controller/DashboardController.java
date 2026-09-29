package com.example.controller;

import com.example.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<?> getManagerDashboard(
            @PathVariable String managerId,
            @RequestParam(required = false) String teamMembers) {
        try {
            List<String> team = teamMembers != null ? 
                Arrays.asList(teamMembers.split(",")) : 
                Arrays.asList();
            
            Map<String, Object> dashboard = dashboardService.getTeamDashboard(managerId, team);
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("Error fetching manager dashboard", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeDashboard(@PathVariable String employeeId) {
        try {
            Map<String, Object> dashboard = dashboardService.getEmployeeDashboard(employeeId);
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("Error fetching employee dashboard", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
