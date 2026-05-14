package com.greenhouse.sensor_service_test.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sensors") @Data
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;
    private Long greenhouseId;

    @Enumerated(EnumType.STRING)
    private Format format;
}