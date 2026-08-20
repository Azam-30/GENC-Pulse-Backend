package com.gencpulse.commit.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommitRequest {

    @NotNull
    private Long employeeId;

    @NotBlank
    private String commitHash;

    @NotBlank
    private String repositoryName;

    @NotBlank
    private String branchName;

    @NotBlank
    private String commitMessage;

    private String commitLink;

    @NotNull
    private LocalDate commitDate;
}