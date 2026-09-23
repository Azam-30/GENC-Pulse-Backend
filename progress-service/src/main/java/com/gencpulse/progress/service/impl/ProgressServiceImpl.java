package com.gencpulse.progress.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.gencpulse.progress.client.EmployeeFeignClient;
import com.gencpulse.progress.dto.ApiResponse;
import com.gencpulse.progress.dto.EmployeeResponse;
import com.gencpulse.progress.dto.ProgressRequest;
import com.gencpulse.progress.dto.ProgressResponse;
import com.gencpulse.progress.entity.Progress;
import com.gencpulse.progress.repository.ProgressRepository;
import com.gencpulse.progress.service.ProgressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository repository;

    private final EmployeeFeignClient employeeFeignClient;

    private final ModelMapper mapper;

    @Override
    public ProgressResponse createProgress(
            ProgressRequest request) {

        try {

            employeeFeignClient.getEmployeeById(
                    request.getEmployeeId());

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Employee does not exist");
        }

        boolean exists =
                repository.existsByEmployeeIdAndUpdateDate(
                        request.getEmployeeId(),
                        request.getUpdateDate());

        if (exists) {

            throw new RuntimeException(
                    "Progress already submitted for today");
        }

        Progress progress = Progress.builder()
                .employeeId(request.getEmployeeId())
                .storyId(request.getStoryId())
                .taskDescription(
                        request.getTaskDescription())
                .hoursWorked(
                        request.getHoursWorked())
                .status(
                        request.getStatus())
                .blockers(
                        request.getBlockers())
                .updateDate(
                        request.getUpdateDate())
                .build();

        Progress savedProgress =
                repository.save(progress);

        return mapper.map(
                savedProgress,
                ProgressResponse.class);
    }

    @Override
    public List<ProgressResponse> getAllProgress() {

        return repository.findAll()
                .stream()
                .map(progress ->
                        mapper.map(
                                progress,
                                ProgressResponse.class))
                .toList();
    }

    @Override
    public ProgressResponse getProgressById(
            Long id) {

        Progress progress =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Progress not found"));

        return mapper.map(
                progress,
                ProgressResponse.class);
    }

    @Override
    public List<ProgressResponse> getProgressByEmployeeId(
            Long employeeId) {

        return repository.findByEmployeeId(employeeId)
                .stream()
                .map(progress ->
                        mapper.map(
                                progress,
                                ProgressResponse.class))
                .toList();
    }
    
    @Override
    public List<ProgressResponse> getProgressByManagerId(
            Long managerId) {

        ApiResponse<List<EmployeeResponse>>
                apiResponse =
                employeeFeignClient
                        .getEmployeesByManagerId(
                                managerId);

        List<EmployeeResponse> employees =
                apiResponse.getData();

        if (employees == null
                || employees.isEmpty()) {

            return List.of();
        }

        List<Long> employeeIds =
                employees.stream()
                        .map(EmployeeResponse::getId)
                        .toList();

        Map<Long, EmployeeResponse> employeeMap =
                employees.stream()
                        .collect(
                                Collectors.toMap(
                                        EmployeeResponse::getId,
                                        Function.identity()));

        return repository
                .findByEmployeeIdIn(
                        employeeIds)
                .stream()
                .map(progress -> {

                    ProgressResponse progressResponse =
                            mapper.map(
                                    progress,
                                    ProgressResponse.class);

                    EmployeeResponse employee =
                            employeeMap.get(
                                    progress.getEmployeeId());

                    if (employee != null) {

                        progressResponse.setEmployeeName(
                                employee.getName());

                        progressResponse.setEmployeeCode(
                                employee.getEmployeeCode());
                    }

                    return progressResponse;

                })
                .toList();
    }

}