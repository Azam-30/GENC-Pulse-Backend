package com.gencpulse.analytics.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gencpulse.analytics.client.CommitClient;
import com.gencpulse.analytics.client.EmployeeClient;
import com.gencpulse.analytics.client.ProgressClient;
import com.gencpulse.analytics.dto.DashboardSummary;
import com.gencpulse.analytics.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl
        implements AnalyticsService {

    private final EmployeeClient employeeClient;

    private final ProgressClient progressClient;

    private final CommitClient commitClient;

    @Override
    public DashboardSummary getDashboardSummary() {

        // Employee Service
        Map<?, ?> employeeResponse =
                (Map<?, ?>) employeeClient.getAllEmployees();

        List<?> employees =
                (List<?>) employeeResponse.get("data");

        // Progress Service
        Object progressResponse =
                progressClient.getAllProgress();

        List<?> progress;

        if (progressResponse instanceof List<?>) {

            progress = (List<?>) progressResponse;

        } else {

            Map<?, ?> progressMap =
                    (Map<?, ?>) progressResponse;

            progress =
                    (List<?>) progressMap.get("data");
        }

        // Commit Service
        Map<?, ?> commitResponse =
                (Map<?, ?>) commitClient.getAllCommits();

        List<?> commits =
                (List<?>) commitResponse.get("data");

        return DashboardSummary.builder()
                .totalEmployees(employees.size())
                .totalProgressUpdates(progress.size())
                .totalCommits(commits.size())
                .blockedTasks(0)
                .build();
    }
}