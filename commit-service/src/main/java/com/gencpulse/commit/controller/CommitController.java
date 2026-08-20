package com.gencpulse.commit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gencpulse.commit.dto.CommitRequest;
import com.gencpulse.commit.dto.CommitResponse;
import com.gencpulse.commit.response.ApiResponse;
import com.gencpulse.commit.service.CommitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/commits")
@RequiredArgsConstructor
public class CommitController {

    private final CommitService commitService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommitResponse>>
    createCommit(
            @Valid @RequestBody CommitRequest request) {

        CommitResponse response =
                commitService.createCommit(request);

        return ResponseEntity.ok(
                ApiResponse.<CommitResponse>builder()
                        .status("SUCCESS")
                        .message("Commit created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommitResponse>>>
    getAllCommits() {

        List<CommitResponse> response =
                commitService.getAllCommits();

        return ResponseEntity.ok(
                ApiResponse.<List<CommitResponse>>
                        builder()
                        .status("SUCCESS")
                        .message("Commits fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommitResponse>>
    getCommitById(
            @PathVariable Long id) {

        CommitResponse response =
                commitService.getCommitById(id);

        return ResponseEntity.ok(
                ApiResponse.<CommitResponse>
                        builder()
                        .status("SUCCESS")
                        .message("Commit fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<CommitResponse>>>
    getByEmployeeId(
            @PathVariable Long employeeId) {

        List<CommitResponse> response =
                commitService.getCommitsByEmployeeId(
                        employeeId);

        return ResponseEntity.ok(
                ApiResponse.<List<CommitResponse>>
                        builder()
                        .status("SUCCESS")
                        .message("Employee commits fetched successfully")
                        .data(response)
                        .build()
        );
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getByDate(
            @PathVariable String date) {

        return ResponseEntity.ok(
                commitService.getCommitsByDate(
                        java.time.LocalDate.parse(
                                date))
        );
    }
    
    @GetMapping("/repository/{repository}")
    public ResponseEntity<?> getByRepository(
            @PathVariable String repository) {

        return ResponseEntity.ok(
                commitService.getCommitsByRepository(
                        repository)
        );
    }
    
    @GetMapping("/count/{employeeId}")
    public ResponseEntity<?> getCommitCount(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                commitService.getCommitCountByEmployee(
                        employeeId)
        );
    }
}