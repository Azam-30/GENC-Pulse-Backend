package com.example.notification.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_log", uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "notification_date", "notification_type"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long employeeId;
    @Column(nullable = false)
    private LocalDate notificationDate;
    @Column(nullable = false, length = 32)
    private String notificationType;
    @Column(nullable = false)
    private Boolean delivered;
}
