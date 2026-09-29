package com.example.repository;

import com.example.entity.GitHubCommit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GitHubCommitRepository extends JpaRepository<GitHubCommit, String> {
    @Query("SELECT g FROM GitHubCommit g WHERE g.employeeId = :employeeId AND DATE(g.commitDate) = :date ORDER BY g.commitDate DESC")
    List<GitHubCommit> findByEmployeeAndDate(
        @Param("employeeId") String employeeId,
        @Param("date") LocalDate date
    );
    @Query("SELECT g FROM GitHubCommit g WHERE g.employeeId = :employeeId AND g.repositoryName = :repo ORDER BY g.commitDate DESC")
    List<GitHubCommit> findByEmployeeAndRepository(
        @Param("employeeId") String employeeId,
        @Param("repo") String repo
    );
}
