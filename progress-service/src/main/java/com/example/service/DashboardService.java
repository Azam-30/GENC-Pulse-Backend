package com.example.service;

import com.example.entity.DailyStatus;
import com.example.repository.DailyStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class DashboardService {
    
    @Autowired
    private DailyStatusRepository dailyStatusRepository;
    
    public Map<String, Object> getTeamDashboard(String managerId, List<String> teamEmployeeIds) {
        log.info("Building dashboard for manager: {}", managerId);
        
        Map<String, Object> dashboard = new HashMap<>();
        LocalDate today = LocalDate.now();
        
        long totalSubmitted = 0;
        long totalPending = 0;
        List<Map<String, Object>> teamStatus = new ArrayList<>();
        
        for (String employeeId : teamEmployeeIds) {
            Optional<DailyStatus> status = dailyStatusRepository.findByEmployeeAndDate(employeeId, today);
            
            Map<String, Object> empStatus = new HashMap<>();
            empStatus.put("employeeId", employeeId);
            
            if (status.isPresent()) {
                DailyStatus s = status.get();
                empStatus.put("submitted", true);
                empStatus.put("submittedAt", s.getSubmittedAt().toString());
                empStatus.put("description", s.getStatusDescription());
                totalSubmitted++;
            } else {
                empStatus.put("submitted", false);
                empStatus.put("submittedAt", "Pending");
                totalPending++;
            }
            
            teamStatus.add(empStatus);
        }
        
        dashboard.put("date", today.toString());
        dashboard.put("totalTeamMembers", teamEmployeeIds.size());
        dashboard.put("submitted", totalSubmitted);
        dashboard.put("pending", totalPending);
        dashboard.put("submissionRate", totalSubmitted * 100 / (totalSubmitted + totalPending) + "%");
        dashboard.put("teamStatus", teamStatus);
        
        return dashboard;
    }
    
    public Map<String, Object> getEmployeeDashboard(String employeeId) {
        log.info("Building dashboard for employee: {}", employeeId);
        
        Map<String, Object> dashboard = new HashMap<>();
        LocalDate today = LocalDate.now();
        
        Optional<DailyStatus> todayStatus = dailyStatusRepository.findByEmployeeAndDate(employeeId, today);
        
        dashboard.put("date", today.toString());
        dashboard.put("statusSubmitted", todayStatus.isPresent());
        
        if (todayStatus.isPresent()) {
            DailyStatus status = todayStatus.get();
            dashboard.put("submittedAt", status.getSubmittedAt().toString());
            dashboard.put("description", status.getStatusDescription());
        } else {
            dashboard.put("submittedAt", "Not submitted");
            dashboard.put("description", "");
        }
        
        List<DailyStatus> lastSevenDays = dailyStatusRepository.findByEmployeeOrderByDateDesc(employeeId);
        dashboard.put("lastSevenDays", lastSevenDays.size());
        dashboard.put("consistencyRate", (lastSevenDays.size() * 100 / 7) + "%");
        
        return dashboard;
    }
}
