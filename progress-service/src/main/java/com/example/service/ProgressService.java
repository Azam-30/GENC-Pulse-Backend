package com.example.service;

import com.example.dto.DailyStatusDTO;
import com.example.entity.DailyStatus;
import com.example.entity.StatusEnum;
import com.example.repository.DailyStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgressService {
    
    @Autowired
    private DailyStatusRepository dailyStatusRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public DailyStatusDTO submitStatus(String employeeId, String statusDescription) {
        log.info("Submitting status for employee: {}", employeeId);
        
        LocalDate today = LocalDate.now();
        
        Optional<DailyStatus> existing = dailyStatusRepository.findByEmployeeAndDate(employeeId, today);
        
        DailyStatus status;
        if (existing.isPresent()) {
            status = existing.get();
            status.setStatusDescription(statusDescription);
            status.setStatus(StatusEnum.SUBMITTED);
            status.setSubmittedAt(LocalDateTime.now());
        } else {
            status = DailyStatus.builder()
                .employeeId(employeeId)
                .statusDescription(statusDescription)
                .statusDate(today)
                .submittedAt(LocalDateTime.now())
                .status(StatusEnum.SUBMITTED)
                .build();
        }
        
        DailyStatus saved = dailyStatusRepository.save(status);
        log.info("Status submitted successfully for employee: {}", employeeId);
        return modelMapper.map(saved, DailyStatusDTO.class);
    }
    
    public Optional<DailyStatusDTO> getStatusByEmployeeAndDate(String employeeId, LocalDate date) {
        Optional<DailyStatus> status = dailyStatusRepository.findByEmployeeAndDate(employeeId, date);
        return status.map(s -> modelMapper.map(s, DailyStatusDTO.class));
    }
    
    public List<DailyStatusDTO> getEmployeeHistory(String employeeId) {
        List<DailyStatus> statuses = dailyStatusRepository.findByEmployeeOrderByDateDesc(employeeId);
        return statuses.stream()
            .map(s -> modelMapper.map(s, DailyStatusDTO.class))
            .collect(Collectors.toList());
    }
    
    public List<DailyStatusDTO> getPendingStatusesByDate(LocalDate date) {
        List<DailyStatus> pending = dailyStatusRepository.findPendingByDate(date);
        return pending.stream()
            .map(s -> modelMapper.map(s, DailyStatusDTO.class))
            .collect(Collectors.toList());
    }
    
    public DailyStatusDTO getTodayStatus(String employeeId) {
        LocalDate today = LocalDate.now();
        Optional<DailyStatus> status = dailyStatusRepository.findByEmployeeAndDate(employeeId, today);
        return status.map(s -> modelMapper.map(s, DailyStatusDTO.class)).orElse(null);
    }
}
