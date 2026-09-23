package com.example.notification.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.notification.entity.NotificationLog;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    boolean existsByEmployeeIdAndNotificationDateAndNotificationType(Long employeeId, LocalDate date, String type);
    long countByEmployeeIdAndNotificationTypeAndNotificationDateBetween(Long employeeId, String type, LocalDate from, LocalDate to);
}
