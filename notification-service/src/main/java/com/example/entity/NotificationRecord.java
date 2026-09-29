package com.example.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_employee_date", columnList = "employee_id,sent_at"),
    @Index(name = "idx_manager_id", columnList = "manager_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String employeeId;
    @Column(nullable = false)
    private String managerId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;
    @Column(nullable = false)
    private Integer dayCount;
    @Column(nullable = false)
    private LocalDateTime sentAt;
    @Column(nullable = false)
    private Boolean emailSent = false;
    @Column(nullable = false)
    private Boolean acknowledged = false;
    private String notificationMessage;
    private LocalDateTime acknowledgedAt;
    
    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
    }
}
