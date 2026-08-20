package com.gencpulse.progress.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gencpulse.progress.dto.ProgressRequest;
import com.gencpulse.progress.service.ProgressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping
    public ResponseEntity<?> createProgress(
            @Valid @RequestBody ProgressRequest request) {

        return ResponseEntity.ok(
                progressService.createProgress(request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(
                progressService.getAllProgress()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                progressService.getProgressById(id)
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                progressService.getProgressByEmployeeId(employeeId)
        );
    }
}