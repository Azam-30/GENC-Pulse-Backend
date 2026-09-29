package com.example.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStatusDTO {
    private String id;
    private String employeeId;
    private String statusDescription;
    private LocalDate statusDate;
    private LocalDateTime submittedAt;
    private String status;
    private List<CommitDTO> commits;
}
