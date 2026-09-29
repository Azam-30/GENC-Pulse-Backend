package com.example.controller;

import com.example.dto.NotificationDTO;
import com.example.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getNotificationsForEmployee(@PathVariable String employeeId) {
        try {
            return ResponseEntity.ok(notificationService.getUnacknowledgedForEmployee(employeeId));
        } catch (Exception e) {
            log.error("Error fetching notifications", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<?> getNotificationsForManager(@PathVariable String managerId) {
        try {
            return ResponseEntity.ok(notificationService.getUnacknowledgedForManager(managerId));
        } catch (Exception e) {
            log.error("Error fetching notifications", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{notificationId}/acknowledge")
    public ResponseEntity<?> acknowledgeNotification(@PathVariable String notificationId) {
        try {
            NotificationDTO notification = notificationService.acknowledgeNotification(notificationId);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            log.error("Error acknowledging notification", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
