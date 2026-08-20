package com.gencpulse.progress.dto;

import java.time.LocalDate;

import com.gencpulse.progress.enums.ProgressStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProgressRequest {

    @NotNull(message = "Employee Id is required")
    private Long employeeId;

    @NotBlank(message = "Story Id is required")
    private String storyId;

    @NotBlank(message = "Task Description is required")
    private String taskDescription;

    @NotNull(message = "Hours Worked is required")
    @Min(value = 1, message = "Minimum 1 hour")
    @Max(value = 24, message = "Maximum 24 hours")
    private Integer hoursWorked;

    @NotNull(message = "Status is required")
    private ProgressStatus status;

    private String blockers;

    @NotNull(message = "Update Date is required")
    private LocalDate updateDate;
}