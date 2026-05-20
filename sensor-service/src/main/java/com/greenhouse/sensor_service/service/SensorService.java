package com.greenhouse.sensor_service.service;

import com.greenhouse.sensor_service.dto.SensorDTO;
import com.greenhouse.sensor_service.model.Sensor;
import com.greenhouse.sensor_service.repository.SensorRepository;
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

    public Sensor update(Long id, SensorDTO dto) {
        Sensor sensor = getById(id);
        sensor.setSerialNumber(dto.getSerialNumber());
        sensor.setGreenhouseId(dto.getGreenhouseId());
        sensor.setFormat(dto.getFormat());
        return sensorRepository.save(sensor);
    }

    public void delete(Long id) {
        sensorRepository.deleteById(id);
    }

    public int deleteByGreenhouseId(Long greenhouseId) {
        List<Sensor> sensors = sensorRepository.findByGreenhouseId(greenhouseId);
        sensorRepository.deleteAll(sensors);
        return sensors.size();
    }
}