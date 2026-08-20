package com.gencpulse.progress.dto;

import java.time.LocalDate;

import com.gencpulse.progress.enums.ProgressStatus;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponse {

    private Long id;

    private Long employeeId;

    private String storyId;

    private String taskDescription;

    private Integer hoursWorked;

    private ProgressStatus status;

    private String blockers;

    private LocalDate updateDate;
}