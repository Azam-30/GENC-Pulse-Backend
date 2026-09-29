package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "github-client", url = "https://api.github.com")
public interface GitHubFeignClient {
    @GetMapping("/repos/{owner}/{repo}/commits")
    List<Map<String, Object>> getCommits(
        @PathVariable String owner,
        @PathVariable String repo,
        @RequestParam("since") String since,
        @RequestParam("until") String until,
        @RequestParam("per_page") int perPage,
        @RequestHeader("Authorization") String authorization
    );
    
    @GetMapping("/user/repos")
    List<Map<String, Object>> getUserRepositories(
        @RequestParam("type") String type,
        @RequestParam("per_page") int perPage,
        @RequestHeader("Authorization") String authorization
    );
}
