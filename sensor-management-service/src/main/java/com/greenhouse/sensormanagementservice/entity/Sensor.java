package com.greenhouse.sensormanagementservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "format")
    private Format format;

    @Column(name = "greenhouse_id")
    private Long greenhouseId;
}