package com.example.repository;

import com.example.entity.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, String> {
    @Query("SELECT n FROM NotificationRecord n WHERE n.employeeId = :employeeId AND n.acknowledged = false ORDER BY n.sentAt DESC")
    List<NotificationRecord> findUnacknowledgedByEmployee(@Param("employeeId") String employeeId);
    @Query("SELECT n FROM NotificationRecord n WHERE n.managerId = :managerId AND n.acknowledged = false ORDER BY n.sentAt DESC")
    List<NotificationRecord> findUnacknowledgedByManager(@Param("managerId") String managerId);
}
