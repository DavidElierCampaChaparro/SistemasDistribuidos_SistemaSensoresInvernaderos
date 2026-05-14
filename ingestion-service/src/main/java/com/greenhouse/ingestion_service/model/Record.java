package com.greenhouse.ingestion_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "records")
@Data
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorSerialNumber;

    private Float temperature;

    private Float humidity;

    private LocalDateTime timestamp;
}