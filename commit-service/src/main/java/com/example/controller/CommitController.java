package com.example.controller;

import com.example.entity.GitHubCommit;
import com.example.service.CommitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commits")
@Slf4j
@CrossOrigin(origins = "*")
public class CommitController {
    
    @Autowired
    private CommitService commitService;
    
    @PostMapping("/sync")
    public ResponseEntity<?> syncCommits(
            @RequestHeader("X-Employee-Id") String employeeId,
            @RequestBody SyncCommitRequest request) {
        try {
            log.info("Syncing commits for employee: {}", employeeId);
            List<Map<String, Object>> commits = commitService.syncCommitsForEmployee(
                employeeId,
                request.getGithubUsername(),
                request.getGithubToken()
            );
            return ResponseEntity.ok(Map.of(
                "message", "Commits synced successfully",
                "count", commits.size()
            ));
        } catch (Exception e) {
            log.error("Error syncing commits", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/employee/{employeeId}/date/{date}")
    public ResponseEntity<?> getCommitsByDate(
            @PathVariable String employeeId,
            @PathVariable String date) {
        try {
            LocalDate statusDate = LocalDate.parse(date);
            List<GitHubCommit> commits = commitService.getCommitsByEmployeeAndDate(employeeId, statusDate);
            return ResponseEntity.ok(commits);
        } catch (Exception e) {
            log.error("Error fetching commits", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/employee/{employeeId}/repo/{repo}")
    public ResponseEntity<?> getCommitsByRepository(
            @PathVariable String employeeId,
            @PathVariable String repo) {
        try {
            List<GitHubCommit> commits = commitService.getCommitsByEmployeeAndRepository(employeeId, repo);
            return ResponseEntity.ok(commits);
        } catch (Exception e) {
            log.error("Error fetching commits", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
}

class SyncCommitRequest {
    private String githubUsername;
    private String githubToken;
    
    public String getGithubUsername() { return githubUsername; }
    public String getGithubToken() { return githubToken; }
    
    public void setGithubUsername(String githubUsername) { this.githubUsername = githubUsername; }
    public void setGithubToken(String githubToken) { this.githubToken = githubToken; }
}
