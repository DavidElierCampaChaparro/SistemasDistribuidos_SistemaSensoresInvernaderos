package com.greenhouse.greenhouse_service.controller;

import com.greenhouse.greenhouse_service.dto.GreenhouseDTO;
import com.greenhouse.greenhouse_service.model.Greenhouse;
import com.greenhouse.greenhouse_service.service.GreenhouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/greenhouses")
@RequiredArgsConstructor
public class GreenhouseController {

    private final GreenhouseService greenhouseService;

    @PostMapping
    public ResponseEntity<Greenhouse> create(@Valid @RequestBody GreenhouseDTO dto) {
        return ResponseEntity.ok(greenhouseService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<Greenhouse>> getAll() {
        return ResponseEntity.ok(greenhouseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Greenhouse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(greenhouseService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Greenhouse> update(@PathVariable Long id, @Valid @RequestBody GreenhouseDTO dto) {
        return ResponseEntity.ok(greenhouseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        greenhouseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}