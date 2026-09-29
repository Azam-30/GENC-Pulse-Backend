package com.example.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    private String id;
    private String email;
    private String name;
    private String role;
    private Boolean active;
    private String githubUsername;
    private String managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
