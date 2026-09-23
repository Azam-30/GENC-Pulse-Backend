package com.example.notification.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "PROGRESSSERVICE")
public interface ProgressClient {
    @GetMapping("/api/progress")
    List<Map<String, Object>> getProgress();
}
