package com.greenhouse.sensor_service.repository;

import com.greenhouse.sensor_service.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findBySerialNumber(String serialNumber);
    List<Sensor> findByGreenhouseId(Long greenhouseId);
}