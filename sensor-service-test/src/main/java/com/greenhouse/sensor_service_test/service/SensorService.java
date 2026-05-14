package com.greenhouse.sensor_service_test.service;

import com.greenhouse.sensor_service_test.dto.SensorDTO;
import com.greenhouse.sensor_service_test.model.Sensor;
import com.greenhouse.sensor_service_test.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;

    public Sensor create(SensorDTO dto) {
        Sensor sensor = new Sensor();
        sensor.setSerialNumber(dto.getSerialNumber());
        sensor.setGreenhouseId(dto.getGreenhouseId());
        sensor.setFormat(dto.getFormat());
        return sensorRepository.save(sensor);
    }

    public List<Sensor> getAll() {
        return sensorRepository.findAll();
    }

    public Sensor getById(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found: " + id));
    }

    public Sensor getBySerialNumber(String serialNumber) {
        return sensorRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("Sensor not found: " + serialNumber));
    }

    public List<Sensor> getByGreenhouse(Long greenhouseId) {
        return sensorRepository.findByGreenhouseId(greenhouseId);
    }

    public void delete(Long id) {
        sensorRepository.deleteById(id);
    }
}