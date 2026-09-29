package com.example.controller;

import com.example.dto.DailyStatusDTO;
import com.example.service.ProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/status")
@Slf4j
@CrossOrigin(origins = "*")
public class ProgressController {
    
    @Autowired
    private ProgressService progressService;
    
    @PostMapping("/submit")
    public ResponseEntity<?> submitStatus(
            @RequestHeader("X-Employee-Id") String employeeId,
            @RequestBody StatusSubmitRequest request) {
        try {
            log.info("Submitting status for employee: {}", employeeId);
            DailyStatusDTO status = progressService.submitStatus(employeeId, request.getStatusDescription());
            return ResponseEntity.status(HttpStatus.CREATED).body(status);
        } catch (Exception e) {
            log.error("Error submitting status", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/today")
    public ResponseEntity<?> getTodayStatus(@RequestHeader("X-Employee-Id") String employeeId) {
        try {
            DailyStatusDTO status = progressService.getTodayStatus(employeeId);
            if (status == null) {
                return ResponseEntity.ok(Map.of("message", "No status submitted today"));
            }
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("Error fetching today's status", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestHeader("X-Employee-Id") String employeeId) {
        try {
            return ResponseEntity.ok(progressService.getEmployeeHistory(employeeId));
        } catch (Exception e) {
            log.error("Error fetching history", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/pending/{date}")
    public ResponseEntity<?> getPendingStatuses(@PathVariable String date) {
        try {
            LocalDate statusDate = LocalDate.parse(date);
            return ResponseEntity.ok(progressService.getPendingStatusesByDate(statusDate));
        } catch (Exception e) {
            log.error("Error fetching pending statuses", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
}

class StatusSubmitRequest {
    private String statusDescription;
    
    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
}
