package com.example.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String id;
    private String employeeId;
    private String managerId;
    private String notificationType;
    private Integer dayCount;
    private String notificationMessage;
    private LocalDateTime sentAt;
    private Boolean acknowledged;
}
