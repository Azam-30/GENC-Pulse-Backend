package com.example.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "github_commits", indexes = {
    @Index(name = "idx_employee_date", columnList = "employee_id,commit_date"),
    @Index(name = "idx_commit_hash", columnList = "commit_hash")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitHubCommit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String employeeId;
    @Column(nullable = false, unique = true)
    private String commitHash;
    @Column(nullable = false)
    private String repositoryName;
    @Lob
    @Column(nullable = false)
    private String commitMessage;
    @Column(nullable = false)
    private LocalDateTime commitDate;
    private String commitUrl;
    private String authorName;
    private Integer filesChanged;
    private Integer additions;
    private Integer deletions;
    @Column(nullable = false, updatable = false)
    private LocalDateTime syncedAt;
    
    @PrePersist
    protected void onCreate() {
        syncedAt = LocalDateTime.now();
    }
}
