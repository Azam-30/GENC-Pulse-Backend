package com.gencpulse.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "CommitService",
        contextId = "analyticsCommitClient"
)
public interface CommitClient {

    @GetMapping("/api/commits")
    Object getAllCommits();
}