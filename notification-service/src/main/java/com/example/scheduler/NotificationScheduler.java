package com.example.scheduler;

import com.example.entity.NotificationType;
import com.example.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class NotificationScheduler {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final Map<String, Integer> missCountMap = new HashMap<>();
    
    @Scheduled(cron = "0 0 17 * * MON-FRI", zone = "Asia/Kolkata")
    public void checkMissingUpdates() {
        log.info("=== Running Daily Status Check at 5 PM ===");
        
        try {
            LocalDate today = LocalDate.now();
            
            List<?> pendingStatuses = getPendingStatuses(today);
            List<?> employees = getAllEmployees();
            
            List<String> submittedEmployeeIds = getSubmittedEmployeeIds();
            
            for (Object emp : employees) {
                Map<String, Object> employee = (Map<String, Object>) emp;
                String employeeId = (String) employee.get("id");
                String managerId = (String) employee.get("managerId");
                String role = (String) employee.get("role");
                
                if ("EMPLOYEE".equals(role) && !submittedEmployeeIds.contains(employeeId)) {
                    handleMissingUpdate(employeeId, managerId);
                }
            }
            
            log.info("=== Daily Status Check Completed ===");
        } catch (Exception e) {
            log.error("Error in daily status check", e);
        }
    }
    
    private void handleMissingUpdate(String employeeId, String managerId) {
        int dayCount = missCountMap.getOrDefault(employeeId, 0) + 1;
        missCountMap.put(employeeId, dayCount);
        
        log.warn("Employee {} missing update for day {}", employeeId, dayCount);
        
        String message = "Employee " + employeeId + " has missed daily status update for " + dayCount + " consecutive days";
        
        NotificationType type = dayCount >= 3 ? NotificationType.THRESHOLD_ALERT : NotificationType.MISSING_UPDATE;
        
        notificationService.createNotification(
            employeeId,
            managerId,
            type,
            dayCount,
            message
        );
    }
    
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Kolkata")
    public void resetMissingUpdateCounters() {
        log.info("Resetting missing update counters");
        missCountMap.clear();
    }
    
    private List<?> getPendingStatuses(LocalDate date) {
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(
                "http://localhost:8082/api/status/pending/" + date,
                List.class
            );
            return (List<?>) response.getBody();
        } catch (Exception e) {
            log.error("Error fetching pending statuses", e);
            return new ArrayList<>();
        }
    }
    
    private List<?> getAllEmployees() {
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(
                "http://localhost:8081/api/employees/managers/all",
                List.class
            );
            return (List<?>) response.getBody();
        } catch (Exception e) {
            log.error("Error fetching employees", e);
            return new ArrayList<>();
        }
    }
    
    private List<String> getSubmittedEmployeeIds() {
        return new ArrayList<>();
    }
}
