package com.greenhouse.sensormanagementservice.controller;

import com.greenhouse.sensormanagementservice.dto.FormatRequestDTO;
import com.greenhouse.sensormanagementservice.dto.FormatResponseDTO;
import com.greenhouse.sensormanagementservice.entity.Format;
import com.greenhouse.sensormanagementservice.service.FormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/formats")
@RequiredArgsConstructor
public class FormatController {

    private final FormatService formatService;

    @PostMapping
    public FormatResponseDTO createFormat(@RequestBody FormatRequestDTO request) {

        Format savedFormat = formatService.saveFormat(request.getName());
        FormatResponseDTO response = new FormatResponseDTO();
        response.setId(savedFormat.getId());
        response.setName(savedFormat.getName());

        return response;
    }
}