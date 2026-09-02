package com.gencpulse.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String employeeCode;

    private String name;

    private String email;

    private String username;

    private String role;

    private Long managerId;

    private String managerName;

    private String projectName;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String designation;

    private String technology;

    private String location;

    private String batch;

    @PrePersist
    public void prePersist() {

        createdAt =
                LocalDateTime.now();

        updatedAt =
                LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}