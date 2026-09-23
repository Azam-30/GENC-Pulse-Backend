package com.example.notification.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.notification.client.EmployeeClient;
import com.example.notification.client.ProgressClient;
import com.example.notification.entity.NotificationLog;
import com.example.notification.repository.NotificationLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplianceNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ComplianceNotificationService.class);

    private final EmployeeClient employeeClient;
    private final ProgressClient progressClient;
    private final NotificationLogRepository logRepository;
    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${gencpulse.notifications.enabled:true}")
    private boolean enabled;

    @Value("${gencpulse.notifications.time-zone:Asia/Kolkata}")
    private String zoneId;

    @Value("${gencpulse.notifications.escalation-missed-days:3}")
    private int escalationMissedDays;

    @Value("${spring.mail.username:no-reply@gencpulse.local}")
    private String from;

    @Scheduled(cron = "${gencpulse.notifications.reminder-cron:0 0 17 * * MON-FRI}", zone = "${gencpulse.notifications.time-zone:Asia/Kolkata}")
    public void checkDailyProgressCompliance() {
        if (!enabled) {
            return;
        }

        LocalDate today = LocalDate.now(ZoneId.of(zoneId));

        try {
            List<Map<String, Object>> employees = extractList(employeeClient.getEmployees());

            Set<Long> submittedEmployeeIds = progressClient.getProgress()
                    .stream()
                    .filter(item -> today.toString().equals(String.valueOf(item.get("updateDate"))))
                    .map(item -> toLong(item.get("employeeId")))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            for (Map<String, Object> employee : employees) {
                if (isActiveEmployee(employee)) {
                    processEmployee(employee, today, submittedEmployeeIds.contains(toLong(employee.get("id"))));
                }
            }
        } catch (Exception ex) {
            log.error("Daily progress compliance check failed", ex);
        }
    }

    private void processEmployee(Map<String, Object> employee, LocalDate today, boolean submittedToday) {
        Long employeeId = toLong(employee.get("id"));
        if (employeeId == null || submittedToday) {
            return;
        }

        String employeeEmail = String.valueOf(employee.getOrDefault("email", "")).trim();
        String employeeName = String.valueOf(employee.getOrDefault("name", "Employee"));

        boolean reminderSent = sendOnce(
                employeeId,
                today,
                "REMINDER",
                employeeEmail,
                "Daily progress update missing",
                "Hello " + employeeName + ",\n\nOur records show that your GenC Pulse progress update for " + today + " was not submitted by 5:00 PM. Please submit it as soon as possible.\n\nRegards,\nGenC Pulse"
        );

        long missedDays = logRepository.countByEmployeeIdAndNotificationTypeAndNotificationDateBetween(
                employeeId,
                "REMINDER",
                today.minusDays(6),
                today
        );

        if (!reminderSent || missedDays < escalationMissedDays) {
            return;
        }

        Long managerId = toLong(employee.get("managerId"));
        if (managerId == null) {
            return;
        }

        try {
            Map<String, Object> managerResponse = extractMap(employeeClient.getEmployee(managerId));
            String managerEmail = String.valueOf(managerResponse.getOrDefault("email", "")).trim();
            String managerName = String.valueOf(managerResponse.getOrDefault("name", "Manager"));

            sendOnce(
                    employeeId,
                    today,
                    "MANAGER_ESCALATION",
                    managerEmail,
                    "Repeated missing progress updates: " + employeeName,
                    "Hello " + managerName + ",\n\n" + employeeName + " has missed " + missedDays + " recent daily progress updates. Please review the situation and follow up with the employee.\n\nRegards,\nGenC Pulse"
            );
        } catch (Exception ex) {
            log.warn("Unable to notify manager for employee {}", employeeId, ex);
        }
    }

    private boolean sendOnce(Long employeeId, LocalDate date, String type, String recipient, String subject, String body) {
        if (recipient == null || recipient.isBlank() || "null".equalsIgnoreCase(recipient)) {
            return false;
        }

        if (logRepository.existsByEmployeeIdAndNotificationDateAndNotificationType(employeeId, date, type)) {
            return false;
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        boolean delivered = false;

        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(from);
                message.setTo(recipient);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                delivered = true;
            } catch (Exception ex) {
                log.error("Notification delivery failed for {}", recipient, ex);
            }
        } else {
            log.warn("SMTP is not configured; skipping notification to {} for type {}", recipient, type);
        }

        logRepository.save(NotificationLog.builder()
                .employeeId(employeeId)
                .notificationDate(date)
                .notificationType(type)
                .delivered(delivered)
                .build());

        return delivered;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractList(Map<String, Object> response) {
        if (response == null) {
            return List.of();
        }

        Object data = response.get("data");
        if (data instanceof List<?> list) {
            return (List<Map<String, Object>>) list;
        }
        return List.of();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractMap(Map<String, Object> response) {
        if (response == null) {
            return Map.of();
        }

        Object data = response.get("data");
        if (data instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return response;
    }

    private boolean isActiveEmployee(Map<String, Object> employee) {
        return "EMPLOYEE".equalsIgnoreCase(String.valueOf(employee.get("role")))
                && !Boolean.FALSE.equals(employee.get("active"));
    }

    private Long toLong(Object value) {
        if (value == null) {
            return null;
        }

        try {
            if (value instanceof Number number) {
                return number.longValue();
            }
            return Long.parseLong(String.valueOf(value));
        } catch (Exception ex) {
            return null;
        }
    }
}
