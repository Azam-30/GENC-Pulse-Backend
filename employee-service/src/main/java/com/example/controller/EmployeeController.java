package com.example.controller;

import com.example.dto.EmployeeDTO;
import com.example.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@Slf4j
@CrossOrigin(origins = "*")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@RequestBody RegisterRequest request) {
        log.info("Register request for: {}", request.getEmail());
        try {
            EmployeeDTO employee = employeeService.registerEmployee(
                request.getEmail(),
                request.getName(),
                request.getPassword(),
                request.getRole()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/me/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id) {
        try {
            EmployeeDTO employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/team/{managerId}")
    public ResponseEntity<?> getTeam(@PathVariable String managerId) {
        try {
            List<EmployeeDTO> team = employeeService.getTeamByManager(managerId);
            return ResponseEntity.ok(team);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{employeeId}/github")
    public ResponseEntity<?> linkGitHub(
            @PathVariable String employeeId,
            @RequestBody GitHubLinkRequest request) {
        try {
            EmployeeDTO employee = employeeService.updateGitHubAccount(
                employeeId,
                request.getGithubUsername(),
                request.getGithubToken()
            );
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/managers/all")
    public ResponseEntity<?> getAllManagers() {
        try {
            List<EmployeeDTO> managers = employeeService.getAllManagers();
            return ResponseEntity.ok(managers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
}

class RegisterRequest {
    private String email;
    private String name;
    private String password;
    private String role;
    
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}

class GitHubLinkRequest {
    private String githubUsername;
    private String githubToken;
    
    public String getGithubUsername() { return githubUsername; }
    public String getGithubToken() { return githubToken; }
    
    public void setGithubUsername(String githubUsername) { this.githubUsername = githubUsername; }
    public void setGithubToken(String githubToken) { this.githubToken = githubToken; }
}
