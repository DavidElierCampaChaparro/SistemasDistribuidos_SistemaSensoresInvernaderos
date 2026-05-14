package com.greenhouse.sensor_service_test.dto;

import com.greenhouse.sensor_service_test.model.Format;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorDTO {
    @NotNull
    private String serialNumber;
    @NotNull
    private Long greenhouseId;
    @NotNull
    private Format format;
}