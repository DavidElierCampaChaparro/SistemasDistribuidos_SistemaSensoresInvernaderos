package com.greenhouse.greenhouse_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GreenhouseDTO {
    @NotNull
    private String name;
    @NotNull
    private String location;
    @NotNull
    private Long ownerId;
    private Float triggerTemperature;
    private Float triggerHumidity;
}