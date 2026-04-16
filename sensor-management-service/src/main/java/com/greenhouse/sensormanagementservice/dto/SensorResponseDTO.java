package com.greenhouse.sensormanagementservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SensorResponseDTO {

    private String serialNumber;
    private Long formatId;
    private Long greenhouseId;

}
