package com.greenhouse.sensor_service.dto;

import com.greenhouse.common.enums.Format;
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