package com.greenhouse.ingestion_service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class SensorDataDTO {
    @NotNull
    private String sensorSerialNumber;
    @NotNull
    private String rawData;
}