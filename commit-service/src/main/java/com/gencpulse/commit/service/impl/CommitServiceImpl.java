package com.gencpulse.commit.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.gencpulse.commit.client.EmployeeFeignClient;
import com.gencpulse.commit.dto.ApiResponse;
import com.gencpulse.commit.dto.CommitRequest;
import com.gencpulse.commit.dto.CommitResponse;
import com.gencpulse.commit.dto.EmployeeResponse;
import com.gencpulse.commit.entity.CommitEntity;
import com.gencpulse.commit.repository.CommitRepository;
import com.gencpulse.commit.service.CommitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommitServiceImpl implements CommitService {

    private final CommitRepository repository;

    private final EmployeeFeignClient employeeFeignClient;

    private final ModelMapper mapper;

    @Override
    public CommitResponse createCommit(
            CommitRequest request) {

        try {

            employeeFeignClient.getEmployeeById(
                    request.getEmployeeId());

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Employee does not exist");
        }

        if (repository.existsByCommitHash(
                request.getCommitHash())) {

            throw new RuntimeException(
                    "Commit already exists");
        }

        CommitEntity commit = CommitEntity.builder()
                .employeeId(request.getEmployeeId())
                .commitHash(request.getCommitHash())
                .repositoryName(request.getRepositoryName())
                .branchName(request.getBranchName())
                .commitMessage(request.getCommitMessage())
                .commitLink(request.getCommitLink())
                .commitDate(request.getCommitDate())
                .build();

        CommitEntity savedCommit =
                repository.save(commit);

        return mapper.map(
                savedCommit,
                CommitResponse.class);
    }

    @Override
    public List<CommitResponse> getAllCommits() {

        return repository.findAll()
                .stream()
                .map(commit ->
                        mapper.map(
                                commit,
                                CommitResponse.class))
                .toList();
    }

    @Override
    public CommitResponse getCommitById(
            Long id) {

        CommitEntity commit =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Commit not found with id : " + id));

        return mapper.map(
                commit,
                CommitResponse.class);
    }

    @Override
    public List<CommitResponse>
    getCommitsByEmployeeId(
            Long employeeId) {

        return repository.findByEmployeeId(employeeId)
                .stream()
                .map(commit ->
                        mapper.map(
                                commit,
                                CommitResponse.class))
                .toList();
    }
    @Override
    public List<CommitResponse> getCommitsByDate(
            LocalDate date) {

        return repository.findByCommitDate(date)
                .stream()
                .map(commit ->
                        mapper.map(
                                commit,
                                CommitResponse.class))
                .toList();
    }

    @Override
    public List<CommitResponse> getCommitsByRepository(
            String repositoryName) {

        return repository.findByRepositoryName(
                        repositoryName)
                .stream()
                .map(commit ->
                        mapper.map(
                                commit,
                                CommitResponse.class))
                .toList();
    }

    @Override
    public long getCommitCountByEmployee(
            Long employeeId) {

        return repository.countByEmployeeId(
                employeeId);
    }

    @Override
    public List<CommitResponse>
    getCommitsByManagerId(
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
                .map(commit -> {

                    CommitResponse commitResponse =
                            mapper.map(
                                    commit,
                                    CommitResponse.class);

                    EmployeeResponse employee =
                            employeeMap.get(
                                    commit.getEmployeeId());

                    if (employee != null) {

                        commitResponse
                                .setEmployeeName(
                                        employee.getName());

                        commitResponse
                                .setEmployeeCode(
                                        employee.getEmployeeCode());
                    }

                    return commitResponse;

                })
                .toList();
    }
    
}