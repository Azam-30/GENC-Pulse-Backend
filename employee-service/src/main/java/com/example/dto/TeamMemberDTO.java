package com.example.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberDTO {
    private String employeeId;
    private String name;
    private String email;
    private String githubUsername;
    private Boolean statusSubmittedToday;
    private String lastSubmissionTime;
    private Integer commitsToday;
    private String statusDescription;
}
