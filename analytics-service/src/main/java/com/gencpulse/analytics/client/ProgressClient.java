package com.gencpulse.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "ProgressService",
        contextId = "analyticsProgressClient"
)
public interface ProgressClient {

    @GetMapping("/api/progress")
    Object getAllProgress();
}
