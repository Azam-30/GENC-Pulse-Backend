package com.gencpulse.employee.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.gencpulse.employee.dto.EmployeeRequest;
import com.gencpulse.employee.dto.EmployeeResponse;
import com.gencpulse.employee.entity.Employee;
import com.gencpulse.employee.exception.DuplicateResourceException;
import com.gencpulse.employee.exception.ResourceNotFoundException;
import com.gencpulse.employee.repository.EmployeeRepository;
import com.gencpulse.employee.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final ModelMapper mapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        if (repository.findByEmployeeCode(
                request.getEmployeeCode()).isPresent()) {

            throw new DuplicateResourceException(
                    "Employee Code already exists");
        }

        if (repository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists");
        }

        Employee employee =
                mapper.map(request, Employee.class);

        Employee savedEmployee =
                repository.save(employee);

        return mapper.map(
                savedEmployee,
                EmployeeResponse.class);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return repository.findAll()
                .stream()
                .map(employee ->
                        mapper.map(
                                employee,
                                EmployeeResponse.class))
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with id : " + id));

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
                                        "Employee not found with id : " + id));

        employee.setEmployeeCode(
                request.getEmployeeCode());

        employee.setName(
                request.getName());

        employee.setEmail(
                request.getEmail());

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

        employee.setManagerName(
                request.getManagerName());

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
                                        "Employee not found with id : " + id));

        repository.delete(employee);
    }
}