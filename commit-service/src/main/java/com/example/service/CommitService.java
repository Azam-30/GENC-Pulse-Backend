package com.example.service;

import com.example.client.GitHubFeignClient;
import com.example.entity.GitHubCommit;
import com.example.repository.GitHubCommitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommitService {
    
    @Autowired
    private GitHubCommitRepository commitRepository;
    
    @Autowired(required = false)
    private GitHubFeignClient gitHubFeignClient;
    
    public List<Map<String, Object>> syncCommitsForEmployee(
            String employeeId,
            String githubUsername,
            String githubToken) {
        log.info("Syncing commits for employee: {} with GitHub user: {}", employeeId, githubUsername);
        
        try {
            String auth = "token " + githubToken;
            LocalDate yesterday = LocalDate.now().minusDays(1);
            LocalDate today = LocalDate.now();
            
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            String since = yesterday.format(formatter);
            String until = today.plusDays(1).format(formatter);
            
            List<Map<String, Object>> commits = gitHubFeignClient.getCommits(
                githubUsername,
                "GENC-Pulse-Backend",
                since,
                until,
                100,
                auth
            );
            
            commits.forEach(commit -> processAndStoreCommit(employeeId, commit));
            
            log.info("Successfully synced {} commits for employee: {}", commits.size(), employeeId);
            return commits;
        } catch (Exception e) {
            log.error("Error syncing commits", e);
            return List.of();
        }
    }
    
    private void processAndStoreCommit(String employeeId, Map<String, Object> commit) {
        try {
            String hash = (String) commit.get("sha");
            
            if (commitRepository.existsByHash(hash)) {
                log.debug("Commit already exists: {}", hash);
                return;
            }
            
            Map<String, Object> commitData = (Map<String, Object>) commit.get("commit");
            Map<String, Object> author = (Map<String, Object>) commitData.get("author");
            
            GitHubCommit githubCommit = GitHubCommit.builder()
                .employeeId(employeeId)
                .commitHash(hash)
                .repositoryName("GENC-Pulse-Backend")
                .commitMessage((String) commitData.get("message"))
                .commitUrl((String) commit.get("html_url"))
                .authorName((String) author.get("name"))
                .build();
            
            commitRepository.save(githubCommit);
            log.debug("Stored commit: {}", hash);
        } catch (Exception e) {
            log.error("Error processing commit", e);
        }
    }
    
    public List<GitHubCommit> getCommitsByEmployeeAndDate(String employeeId, LocalDate date) {
        return commitRepository.findByEmployeeAndDate(employeeId, date);
    }
    
    public List<GitHubCommit> getCommitsByEmployeeAndRepository(String employeeId, String repo) {
        return commitRepository.findByEmployeeAndRepository(employeeId, repo);
    }
}
