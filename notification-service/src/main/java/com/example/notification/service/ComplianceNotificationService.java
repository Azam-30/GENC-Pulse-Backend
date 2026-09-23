package com.example.notification.service;

import java.time.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Optional<JavaMailSender> mailSender;

    @Value("${gencpulse.notifications.enabled:true}") private boolean enabled;
    @Value("${gencpulse.notifications.time-zone:Asia/Kolkata}") private String zoneId;
    @Value("${gencpulse.notifications.escalation-missed-days:3}") private int escalationMissedDays;
    @Value("${spring.mail.username:no-reply@gencpulse.local}") private String from;

    /** Runs at 17:00 Monday-Friday in the configured business timezone. */
    @Scheduled(cron = "${gencpulse.notifications.reminder-cron:0 0 17 * * MON-FRI}", zone = "${gencpulse.notifications.time-zone:Asia/Kolkata}")
    public void checkDailyProgressCompliance() {
        if (!enabled) return;
        LocalDate today = LocalDate.now(ZoneId.of(zoneId));
        try {
            List<Map<String, Object>> employees = data(employeeClient.getEmployees());
            Set<Long> submitted = progressClient.getProgress().stream()
                .filter(p -> today.toString().equals(String.valueOf(p.get("updateDate"))))
                .map(p -> number(p.get("employeeId"))).filter(Objects::nonNull).collect(java.util.stream.Collectors.toSet());

            employees.stream().filter(this::isActiveEmployee).forEach(employee -> process(employee, today, submitted.contains(number(employee.get("id")))));
        } catch (Exception ex) {
            log.error("Daily progress compliance check failed", ex);
        }
    }

    private void process(Map<String, Object> employee, LocalDate date, boolean submitted) {
        Long employeeId = number(employee.get("id"));
        if (employeeId == null || submitted) return;
        boolean reminderSent = sendOnce(employeeId, date, "REMINDER", String.valueOf(employee.get("email")),
            "Daily progress update missing", "Hello " + employee.get("name") + ",\n\nOur records show that your GenC Pulse progress update for " + date + " was not submitted by 5:00 PM. Please submit it as soon as possible.\n\nRegards,\nGenC Pulse");
        long missedDays = logRepository.countByEmployeeIdAndNotificationTypeAndNotificationDateBetween(employeeId, "REMINDER", date.minusDays(6), date);
        if (reminderSent && missedDays >= escalationMissedDays) {
            Long managerId = number(employee.get("managerId"));
            if (managerId != null) {
                try {
                    Map<String, Object> manager = unwrap(employeeClient.getEmployee(managerId));
                    sendOnce(employeeId, date, "MANAGER_ESCALATION", String.valueOf(manager.get("email")),
                        "Repeated missing progress updates: " + employee.get("name"),
                        "Hello " + manager.get("name") + ",\n\n" + employee.get("name") + " has missed " + missedDays + " recent daily progress updates. Please review the situation.\n\nGenC Pulse");
                } catch (Exception ex) { log.warn("Unable to notify manager for employee {}", employeeId, ex); }
            }
        }
    }

    private boolean sendOnce(Long employeeId, LocalDate date, String type, String recipient, String subject, String body) {
        if (recipient == null || recipient.isBlank() || "null".equalsIgnoreCase(recipient) || logRepository.existsByEmployeeIdAndNotificationDateAndNotificationType(employeeId, date, type)) return false;
        boolean delivered = false;
        try {
            if (mailSender.isPresent()) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(from); message.setTo(recipient); message.setSubject(subject); message.setText(body);
                mailSender.get().send(message); delivered = true;
            } else log.warn("SMTP is not configured; notification for {} was not sent", recipient);
        } catch (Exception ex) { log.error("Notification delivery failed for {}", recipient, ex); }
        logRepository.save(NotificationLog.builder().employeeId(employeeId).notificationDate(date).notificationType(type).delivered(delivered).build());
        return delivered;
    }

    @SuppressWarnings("unchecked") private List<Map<String, Object>> data(Map<String, Object> response) {
        Object value = response == null ? null : response.get("data");
        return value instanceof List<?> list ? (List<Map<String, Object>>) list : List.of();
    }
    @SuppressWarnings("unchecked") private Map<String, Object> unwrap(Map<String, Object> response) {
        Object value = response == null ? null : response.get("data");
        return value instanceof Map<?, ?> map ? (Map<String, Object>) map : response;
    }
    private boolean isActiveEmployee(Map<String, Object> e) { return "EMPLOYEE".equalsIgnoreCase(String.valueOf(e.get("role"))) && !Boolean.FALSE.equals(e.get("active")); }
    private Long number(Object value) { try { return value == null ? null : value instanceof Number n ? n.longValue() : Long.valueOf(String.valueOf(value)); } catch (Exception ex) { return null; } }
}
