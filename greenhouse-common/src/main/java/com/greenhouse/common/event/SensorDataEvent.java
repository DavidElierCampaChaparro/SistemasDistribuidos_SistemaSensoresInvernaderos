package com.greenhouse.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataEvent implements Serializable {
    private String sensorSerialNumber;
    private Long greenhouseId;
    private Float temperature;
    private Float humidity;
    private LocalDateTime timestamp;
}