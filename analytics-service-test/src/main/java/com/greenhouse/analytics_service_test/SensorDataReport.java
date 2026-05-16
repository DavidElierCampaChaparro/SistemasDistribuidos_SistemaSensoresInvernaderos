package com.greenhouse.analytics_service_test;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "sensorDataReport")
@XmlAccessorType(XmlAccessType.FIELD)
public class SensorDataReport {

    private Long greenhouseId;
    private String from;
    private String to;

    @XmlElementWrapper(name = "records")
    @XmlElement(name = "record")
    private List<SensorRecordXml> records;
}