package com.example.repository;

import com.example.entity.DailyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyStatusRepository extends JpaRepository<DailyStatus, String> {
    @Query("SELECT d FROM DailyStatus d WHERE d.employeeId = :employeeId AND d.statusDate = :date")
    Optional<DailyStatus> findByEmployeeAndDate(
        @Param("employeeId") String employeeId,
        @Param("date") LocalDate date
    );
    @Query("SELECT d FROM DailyStatus d WHERE d.statusDate = :date AND d.status = 'PENDING'")
    List<DailyStatus> findPendingByDate(@Param("date") LocalDate date);
    @Query("SELECT d FROM DailyStatus d WHERE d.employeeId = :employeeId ORDER BY d.statusDate DESC")
    List<DailyStatus> findByEmployeeOrderByDateDesc(@Param("employeeId") String employeeId);
}
