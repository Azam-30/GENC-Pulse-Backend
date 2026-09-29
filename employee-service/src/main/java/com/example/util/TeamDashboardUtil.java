package com.example.util;

import com.example.dto.TeamMemberDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Component
public class TeamDashboardUtil {
    
    public static TeamMemberDTO buildTeamMemberView(
            String employeeId,
            String name,
            String email,
            String githubUsername,
            Map<String, Object> statusInfo,
            int commitCount) {
        
        Boolean statusSubmitted = false;
        String lastSubmissionTime = "Never";
        String statusDescription = "No status";
        
        if (statusInfo != null) {
            statusSubmitted = (Boolean) statusInfo.getOrDefault("submitted", false);
            lastSubmissionTime = (String) statusInfo.getOrDefault("submittedAt", "Never");
            statusDescription = (String) statusInfo.getOrDefault("description", "No status");
        }
        
        return TeamMemberDTO.builder()
            .employeeId(employeeId)
            .name(name)
            .email(email)
            .githubUsername(githubUsername)
            .statusSubmittedToday(statusSubmitted)
            .lastSubmissionTime(lastSubmissionTime)
            .commitsToday(commitCount)
            .statusDescription(statusDescription)
            .build();
    }
}
