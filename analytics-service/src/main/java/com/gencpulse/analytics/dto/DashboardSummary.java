package com.gencpulse.analytics.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummary {

    private Integer totalEmployees;

    private Integer totalProgressUpdates;

    private Integer totalCommits;

    private Integer blockedTasks;
}
