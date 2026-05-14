package com.greenhouse.sensor_service_test.controller;

import com.greenhouse.sensor_service_test.dto.SensorDTO;
import com.greenhouse.sensor_service_test.model.Sensor;
import com.greenhouse.sensor_service_test.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    public ResponseEntity<Sensor> create(@Valid @RequestBody SensorDTO dto) {
        return ResponseEntity.ok(sensorService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<Sensor>> getAll() {
        return ResponseEntity.ok(sensorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sensorService.getById(id));
    }

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<Sensor> getBySerialNumber(@PathVariable String serialNumber) {
        return ResponseEntity.ok(sensorService.getBySerialNumber(serialNumber));
    }

    @GetMapping("/greenhouse/{greenhouseId}")
    public ResponseEntity<List<Sensor>> getByGreenhouse(@PathVariable Long greenhouseId) {
        return ResponseEntity.ok(sensorService.getByGreenhouse(greenhouseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sensorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}