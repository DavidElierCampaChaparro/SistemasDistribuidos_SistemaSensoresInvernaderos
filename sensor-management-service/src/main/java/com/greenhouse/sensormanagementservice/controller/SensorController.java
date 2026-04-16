package com.greenhouse.sensormanagementservice.controller;

import com.greenhouse.sensormanagementservice.dto.SensorRequestDTO;
import com.greenhouse.sensormanagementservice.dto.SensorResponseDTO;
import com.greenhouse.sensormanagementservice.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    public ResponseEntity<SensorResponseDTO> create(@Valid @RequestBody SensorRequestDTO request) {

        SensorResponseDTO responseData = sensorService.saveSensor(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }
}
