package com.greenhouse.notification_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
@Data
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorSerialNumber;
    private Long greenhouseId;
    private Long ownerId;
    private String emailSentTo;
    private boolean temperatureExceeded;
    private boolean humidityExceeded;
    private String status;
    private LocalDateTime sentAt;
}