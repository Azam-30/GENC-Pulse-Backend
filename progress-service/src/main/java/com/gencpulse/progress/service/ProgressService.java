package com.gencpulse.progress.service;

import java.util.List;

import com.gencpulse.progress.dto.ProgressRequest;
import com.gencpulse.progress.dto.ProgressResponse;

public interface ProgressService {

    ProgressResponse createProgress(
            ProgressRequest request);

    List<ProgressResponse> getAllProgress();

    ProgressResponse getProgressById(
            Long id);

    List<ProgressResponse> getProgressByEmployeeId(
            Long employeeId);
}
