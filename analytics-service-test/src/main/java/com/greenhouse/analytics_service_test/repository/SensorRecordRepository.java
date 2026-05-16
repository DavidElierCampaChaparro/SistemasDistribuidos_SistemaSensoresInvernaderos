package com.greenhouse.analytics_service_test.repository;

import com.greenhouse.analytics_service_test.model.SensorRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorRecordRepository extends JpaRepository<SensorRecord, Long> {

    List<SensorRecord> findBySensorSerialNumberAndTimestampBetween(
            String sensorSerialNumber, LocalDateTime from, LocalDateTime to);

    List<SensorRecord> findByGreenhouseIdAndTimestampBetween(
            Long greenhouseId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT AVG(r.temperature), AVG(r.humidity) FROM SensorRecord r " +
            "WHERE r.greenhouseId = :greenhouseId " +
            "AND r.timestamp BETWEEN :from AND :to")
    List<Object[]> findAverageByGreenhouseIdAndTimestampBetween(
            @Param("greenhouseId") Long greenhouseId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
