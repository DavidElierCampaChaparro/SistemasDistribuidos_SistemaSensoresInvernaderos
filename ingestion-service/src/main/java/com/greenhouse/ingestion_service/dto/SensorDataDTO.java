package com.greenhouse.ingestion_service.dto;

import com.greenhouse.ingestion_service.model.Format;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class SensorDataDTO {

    @NotNull
    private String sensorSerialNumber;

    @NotNull
    private Format format;

    @NotNull
    private String rawData;
}