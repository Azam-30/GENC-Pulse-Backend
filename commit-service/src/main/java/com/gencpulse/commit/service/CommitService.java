package com.gencpulse.commit.service;

import java.time.LocalDate;
import java.util.List;

import com.gencpulse.commit.dto.CommitRequest;
import com.gencpulse.commit.dto.CommitResponse;

public interface CommitService {

    CommitResponse createCommit(
            CommitRequest request);

    List<CommitResponse> getAllCommits();

    CommitResponse getCommitById(
            Long id);

    List<CommitResponse> getCommitsByEmployeeId(
            Long employeeId);

    List<CommitResponse> getCommitsByDate(
            LocalDate date);

    List<CommitResponse> getCommitsByRepository(
            String repositoryName);

    long getCommitCountByEmployee(
            Long employeeId);
}
