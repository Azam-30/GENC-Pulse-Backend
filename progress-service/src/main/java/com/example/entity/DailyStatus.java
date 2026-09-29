package com.example.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_status", indexes = {
    @Index(name = "idx_employee_date", columnList = "employee_id,status_date"),
    @Index(name = "idx_status_date", columnList = "status_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String employeeId;
    @Lob
    @Column(nullable = false)
    private String statusDescription;
    @Column(nullable = false)
    private LocalDate statusDate;
    private LocalDateTime submittedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = StatusEnum.PENDING;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
