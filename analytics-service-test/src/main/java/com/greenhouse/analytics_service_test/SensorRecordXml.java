package com.greenhouse.analytics_service_test;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SensorRecordXml {

    private String sensorSerialNumber;
    private Float temperature;
    private Float humidity;
    private String timestamp;
}