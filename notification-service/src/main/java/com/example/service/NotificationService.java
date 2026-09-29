package com.example.service;

import com.example.dto.NotificationDTO;
import com.example.entity.NotificationRecord;
import com.example.entity.NotificationType;
import com.example.repository.NotificationRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {
    
    @Autowired
    private NotificationRecordRepository notificationRepository;
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    public NotificationDTO createNotification(
            String employeeId,
            String managerId,
            NotificationType type,
            int dayCount,
            String message) {
        log.info("Creating notification for employee: {} with type: {}", employeeId, type);
        
        NotificationRecord notification = NotificationRecord.builder()
            .employeeId(employeeId)
            .managerId(managerId)
            .notificationType(type)
            .dayCount(dayCount)
            .notificationMessage(message)
            .emailSent(false)
            .acknowledged(false)
            .build();
        
        NotificationRecord saved = notificationRepository.save(notification);
        
        if (dayCount >= 3) {
            sendEmailNotification(saved);
        }
        
        return convertToDTO(saved);
    }
    
    private void sendEmailNotification(NotificationRecord notification) {
        try {
            if (mailSender == null) {
                log.warn("Mail sender not configured, skipping email");
                return;
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("manager@genc.com");
            message.setSubject("URGENT: Employee Missing Daily Updates");
            message.setText(notification.getNotificationMessage());
            
            mailSender.send(message);
            notification.setEmailSent(true);
            notificationRepository.save(notification);
            
            log.info("Email notification sent for employee: {}", notification.getEmployeeId());
        } catch (Exception e) {
            log.error("Error sending email notification", e);
        }
    }
    
    public List<NotificationDTO> getUnacknowledgedForEmployee(String employeeId) {
        List<NotificationRecord> notifications = notificationRepository.findUnacknowledgedByEmployee(employeeId);
        return notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<NotificationDTO> getUnacknowledgedForManager(String managerId) {
        List<NotificationRecord> notifications = notificationRepository.findUnacknowledgedByManager(managerId);
        return notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public NotificationDTO acknowledgeNotification(String notificationId) {
        log.info("Acknowledging notification: {}", notificationId);
        NotificationRecord notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setAcknowledged(true);
        notification.setAcknowledgedAt(LocalDateTime.now());
        NotificationRecord saved = notificationRepository.save(notification);
        
        return convertToDTO(saved);
    }
    
    private NotificationDTO convertToDTO(NotificationRecord record) {
        return NotificationDTO.builder()
            .id(record.getId())
            .employeeId(record.getEmployeeId())
            .managerId(record.getManagerId())
            .notificationType(record.getNotificationType().toString())
            .dayCount(record.getDayCount())
            .notificationMessage(record.getNotificationMessage())
            .sentAt(record.getSentAt())
            .acknowledged(record.getAcknowledged())
            .build();
    }
}
