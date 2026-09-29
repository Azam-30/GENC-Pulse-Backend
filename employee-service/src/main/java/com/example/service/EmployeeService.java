package com.example.service;

import com.example.dto.EmployeeDTO;
import com.example.dto.TeamMemberDTO;
import com.example.entity.Employee;
import com.example.entity.Role;
import com.example.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public EmployeeDTO registerEmployee(String email, String name, String password, String role) {
        log.info("Registering employee: {}", email);
        
        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Employee already exists with email: " + email);
        }
        
        Employee employee = Employee.builder()
            .email(email)
            .name(name)
            .password(passwordEncoder.encode(password))
            .role(Role.valueOf(role.toUpperCase()))
            .active(true)
            .build();
        
        Employee saved = employeeRepository.save(employee);
        log.info("Employee registered successfully: {}", saved.getId());
        return modelMapper.map(saved, EmployeeDTO.class);
    }
    
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    public EmployeeDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        return modelMapper.map(employee, EmployeeDTO.class);
    }
    
    public List<EmployeeDTO> getTeamByManager(String managerId) {
        log.info("Fetching team for manager: {}", managerId);
        List<Employee> team = employeeRepository.findTeamByManager(managerId);
        return team.stream()
            .map(e -> modelMapper.map(e, EmployeeDTO.class))
            .collect(Collectors.toList());
    }
    
    public EmployeeDTO updateGitHubAccount(String employeeId, String githubUsername, String githubToken) {
        log.info("Updating GitHub account for employee: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));
        
        employee.setGithubUsername(githubUsername);
        employee.setGithubToken(githubToken);
        Employee updated = employeeRepository.save(employee);
        
        return modelMapper.map(updated, EmployeeDTO.class);
    }
    
    public List<EmployeeDTO> getAllManagers() {
        List<Employee> managers = employeeRepository.findAllManagers();
        return managers.stream()
            .map(e -> modelMapper.map(e, EmployeeDTO.class))
            .collect(Collectors.toList());
    }
}
