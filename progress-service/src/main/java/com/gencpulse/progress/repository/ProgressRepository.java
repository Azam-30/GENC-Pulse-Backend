package com.gencpulse.progress.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gencpulse.progress.entity.Progress;
import com.gencpulse.progress.enums.ProgressStatus;

public interface ProgressRepository
        extends JpaRepository<Progress, Long> {

    List<Progress> findByEmployeeId(Long employeeId);
    
    boolean existsByEmployeeIdAndUpdateDate(
            Long employeeId,
            LocalDate updateDate);
    

    List<Progress> findByStatus(
            ProgressStatus status);

    List<Progress> findByUpdateDate(
            LocalDate updateDate);
    
    List<Progress> findByEmployeeIdIn(
            List<Long> employeeIds);

    
}