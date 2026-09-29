package com.example.controller;

import com.example.entity.Employee;
import com.example.security.JwtTokenProvider;
import com.example.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for: {}", request.getEmail());
        try {
            Optional<Employee> employee = employeeService.findByEmail(request.getEmail());
            
            if (employee.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
            }
            
            Employee emp = employee.get();
            if (!passwordEncoder.matches(request.getPassword(), emp.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
            }
            
            String token = jwtTokenProvider.generateToken(emp.getId(), emp.getEmail(), emp.getRole().toString());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", emp.getId());
            response.put("email", emp.getEmail());
            response.put("name", emp.getName());
            response.put("role", emp.getRole().toString());
            
            log.info("Login successful for: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Login failed"));
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            boolean valid = jwtTokenProvider.validateToken(token);
            return ResponseEntity.ok(Map.of("valid", valid));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("valid", false));
        }
    }
}

class LoginRequest {
    private String email;
    private String password;
    
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
