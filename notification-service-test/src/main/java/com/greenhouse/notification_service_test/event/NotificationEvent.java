package com.greenhouse.notification_service_test.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent implements Serializable {
    private String sensorSerialNumber;
    private Long greenhouseId;
    private boolean temperatureExceeded;
    private boolean humidityExceeded;
}