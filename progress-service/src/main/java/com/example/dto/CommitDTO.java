package com.example.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitDTO {
    private String commitHash;
    private String repositoryName;
    private String commitMessage;
    private LocalDateTime commitDate;
    private String commitUrl;
    private Integer filesChanged;
    private Integer additions;
    private Integer deletions;
}
