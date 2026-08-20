package com.gencpulse.commit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gencpulse.commit.entity.CommitEntity;

public interface CommitRepository
        extends JpaRepository<CommitEntity, Long> {

    boolean existsByCommitHash(
            String commitHash);

    List<CommitEntity> findByEmployeeId(
            Long employeeId);

    List<CommitEntity> findByCommitDate(
            LocalDate commitDate);

    List<CommitEntity> findByRepositoryName(
            String repositoryName);

    long countByEmployeeId(
            Long employeeId);
}