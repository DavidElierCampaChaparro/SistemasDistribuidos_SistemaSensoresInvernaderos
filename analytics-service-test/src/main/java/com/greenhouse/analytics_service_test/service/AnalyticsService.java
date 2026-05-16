package com.greenhouse.analytics_service_test.service;

import com.greenhouse.analytics_service_test.model.SensorRecord;
import com.greenhouse.analytics_service_test.repository.SensorRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final SensorRecordRepository repository;

    public List<SensorRecord> getBySensor(String serialNumber, LocalDateTime from, LocalDateTime to) {
        return repository.findBySensorSerialNumberAndTimestampBetween(serialNumber, from, to);
    }

    public List<SensorRecord> getByGreenhouse(Long greenhouseId, LocalDateTime from, LocalDateTime to) {
        return repository.findByGreenhouseIdAndTimestampBetween(greenhouseId, from, to);
    }

    public Map<String, Double> getAverageByGreenhouse(Long greenhouseId, LocalDateTime from, LocalDateTime to) {
        List<Object[]> result = repository.findAverageByGreenhouseIdAndTimestampBetween(greenhouseId, from, to);
        Object[] row = result.get(0);
        return Map.of(
                "temperature", row[0] != null ? ((Double) row[0]) : 0.0,
                "humidity", row[1] != null ? ((Double) row[1]) : 0.0
        );
    }
}