package com.gencpulse.commit.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitResponse {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private String employeeCode;

    private String commitHash;

    private String repositoryName;

    private String branchName;

    private String commitMessage;

    private String commitLink;

    private LocalDate commitDate;
}