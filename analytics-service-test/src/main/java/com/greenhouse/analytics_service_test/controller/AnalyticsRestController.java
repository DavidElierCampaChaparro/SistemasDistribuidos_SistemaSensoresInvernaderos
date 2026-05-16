package com.greenhouse.analytics_service_test.controller;

import com.greenhouse.analytics_service_test.model.SensorRecord;
import com.greenhouse.analytics_service_test.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsRestController {

    private final AnalyticsService analyticsService;

    @GetMapping("/sensor/{serialNumber}")
    public ResponseEntity<List<SensorRecord>> getBySensor(
            @PathVariable String serialNumber,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return ResponseEntity.ok(analyticsService.getBySensor(serialNumber, from, to));
    }

    @GetMapping("/greenhouse/{greenhouseId}")
    public ResponseEntity<List<SensorRecord>> getByGreenhouse(
            @PathVariable Long greenhouseId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return ResponseEntity.ok(analyticsService.getByGreenhouse(greenhouseId, from, to));
    }

    @GetMapping("/greenhouse/{greenhouseId}/average")
    public ResponseEntity<Map<String, Double>> getAverage(
            @PathVariable Long greenhouseId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return ResponseEntity.ok(analyticsService.getAverageByGreenhouse(greenhouseId, from, to));
    }
}