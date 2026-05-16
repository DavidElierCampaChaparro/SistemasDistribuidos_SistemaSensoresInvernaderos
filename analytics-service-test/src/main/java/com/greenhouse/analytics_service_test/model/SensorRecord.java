package com.greenhouse.analytics_service_test.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sensor_records")
@NoArgsConstructor
@AllArgsConstructor
public class SensorRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorSerialNumber;
    private Long greenhouseId;
    private Float temperature;
    private Float humidity;
    private LocalDateTime timestamp;
}