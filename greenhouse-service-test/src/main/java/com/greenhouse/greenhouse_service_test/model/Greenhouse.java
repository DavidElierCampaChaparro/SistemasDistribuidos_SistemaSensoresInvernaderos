package com.greenhouse.greenhouse_service_test.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "greenhouses") @Data
public class Greenhouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private Long ownerId;
    private Float triggerTemperature;
    private Float triggerHumidity;
}