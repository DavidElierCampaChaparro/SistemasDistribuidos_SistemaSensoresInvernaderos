package com.greenhouse.ingestion_service.controller;

import com.greenhouse.ingestion_service.dto.SensorDataDTO;
import com.greenhouse.ingestion_service.model.Record;
import com.greenhouse.ingestion_service.service.IngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingest")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService ingestionService;

    @PostMapping
    public ResponseEntity<Record> ingest(@Valid @RequestBody SensorDataDTO dto) {
        Record saved = ingestionService.save(dto);
        return ResponseEntity.ok(saved);
    }
}