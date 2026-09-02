package com.gencpulse.employee.repository;

import com.gencpulse.employee.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(
            String employeeCode);

    boolean existsByEmail(
            String email);

    List<Employee> findByRole(
            String role);

    List<Employee> findByManagerId(
            Long managerId);
}
