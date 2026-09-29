package com.example.repository;

import com.example.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    @Query("SELECT e FROM Employee e WHERE e.role = 'MANAGER' AND e.active = true")
    List<Employee> findAllManagers();
    @Query("SELECT e FROM Employee e WHERE e.managerId = :managerId AND e.active = true")
    List<Employee> findTeamByManager(@Param("managerId") String managerId);
    @Query("SELECT e FROM Employee e WHERE e.active = true")
    List<Employee> findAllActive();
}
