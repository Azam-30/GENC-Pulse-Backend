package com.gencpulse.employee.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.gencpulse.employee.client.AuthClient;
import com.gencpulse.employee.dto.EmployeeRequest;
import com.gencpulse.employee.dto.EmployeeResponse;
import com.gencpulse.employee.dto.RegisterRequestDto;
import com.gencpulse.employee.dto.Role;
import com.gencpulse.employee.entity.Employee;
import com.gencpulse.employee.exception.DuplicateResourceException;
import com.gencpulse.employee.exception.ResourceNotFoundException;
import com.gencpulse.employee.repository.EmployeeRepository;
import com.gencpulse.employee.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl
        implements EmployeeService {

    private final EmployeeRepository repository;

    private final ModelMapper mapper;

    private final AuthClient authClient;

    @Override
    public EmployeeResponse createEmployee(
            EmployeeRequest request) {

        if (repository.findByEmployeeCode(
                request.getEmployeeCode())
                .isPresent()) {

            throw new DuplicateResourceException(
                    "Employee Code already exists");
        }

        if (repository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists");
        }

        Employee employee =
                mapper.map(
                        request,
                        Employee.class);

        employee.setUsername(
                request.getUsername());

        if (request.getManagerId() != null) {

            Employee manager =
                    repository.findById(
                            request.getManagerId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Manager not found"));

            if (!"MANAGER".equalsIgnoreCase(
                    manager.getRole())) {

                throw new RuntimeException(
                        "Selected employee is not a manager");
            }

            employee.setManagerId(
                    manager.getId());

            employee.setManagerName(
                    manager.getName());
        }

        Employee savedEmployee =
                repository.save(employee);

        RegisterRequestDto user =
                new RegisterRequestDto();

        user.setEmployeeId(
                savedEmployee.getId());

        user.setUsername(
                request.getUsername());

        user.setEmail(
                request.getEmail());

        user.setPassword(
                request.getPassword());

        user.setRole(
                Role.valueOf(
                        request.getRole()));

        authClient.registerUser(user);

        return mapper.map(
                savedEmployee,
                EmployeeResponse.class);
    }

    @Override
    public List<EmployeeResponse>
    getAllEmployees() {

        return repository.findAll()
                .stream()
                .map(employee ->
                        mapper.map(
                                employee,
                                EmployeeResponse.class))
                .toList();
    }

    @Override
    public EmployeeResponse
    getEmployeeById(Long id) {

        Employee employee =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with id : "
                                                + id));

        return mapper.map(
                employee,
                EmployeeResponse.class);
    }

    @Override
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request) {

        Employee employee =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with id : "
                                                + id));

        employee.setEmployeeCode(
                request.getEmployeeCode());

        employee.setName(
                request.getName());

        employee.setEmail(
                request.getEmail());

        employee.setUsername(
                request.getUsername());

        employee.setRole(
                request.getRole());

        employee.setDesignation(
                request.getDesignation());

        employee.setTechnology(
                request.getTechnology());

        employee.setLocation(
                request.getLocation());

        employee.setBatch(
                request.getBatch());

        if (request.getManagerId() != null) {

            Employee manager =
                    repository.findById(
                            request.getManagerId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Manager not found"));

            if (!"MANAGER".equalsIgnoreCase(
                    manager.getRole())) {

                throw new RuntimeException(
                        "Selected employee is not a manager");
            }

            employee.setManagerId(
                    manager.getId());

            employee.setManagerName(
                    manager.getName());
        } else {

            employee.setManagerId(null);

            employee.setManagerName(null);
        }

        employee.setProjectName(
                request.getProjectName());

        employee.setActive(
                request.getActive());

        Employee updatedEmployee =
                repository.save(employee);

        return mapper.map(
                updatedEmployee,
                EmployeeResponse.class);
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with id : "
                                                + id));

        repository.delete(employee);
    }

    @Override
    public List<EmployeeResponse> getManagers() {

        return repository.findByRole(
                        "MANAGER")
                .stream()
                .map(employee ->
                        mapper.map(
                                employee,
                                EmployeeResponse.class))
                .toList();
    }

    @Override
    public List<EmployeeResponse>
    getEmployeesByManagerId(
            Long managerId) {

        return repository.findByManagerId(
                        managerId)
                .stream()
                .map(employee ->
                        mapper.map(
                                employee,
                                EmployeeResponse.class))
                .toList();
    }
}