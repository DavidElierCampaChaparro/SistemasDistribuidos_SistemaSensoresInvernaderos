package com.greenhouse.greenhouse_service.service;

import com.greenhouse.greenhouse_service.client.SensorGrpcClient;
import com.greenhouse.greenhouse_service.dto.GreenhouseDTO;
import com.greenhouse.greenhouse_service.model.Greenhouse;
import com.greenhouse.greenhouse_service.repository.GreenhouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GreenhouseService {

    private final GreenhouseRepository greenhouseRepository;
    private final SensorGrpcClient sensorGrpcClient;

    public Greenhouse create(GreenhouseDTO dto) {
        Greenhouse greenhouse = new Greenhouse();
        greenhouse.setName(dto.getName());
        greenhouse.setLocation(dto.getLocation());
        greenhouse.setOwnerId(dto.getOwnerId());
        greenhouse.setTriggerTemperature(dto.getTriggerTemperature());
        greenhouse.setTriggerHumidity(dto.getTriggerHumidity());
        return greenhouseRepository.save(greenhouse);
    }

    public List<Greenhouse> getAll() {
        return greenhouseRepository.findAll();
    }

    public Greenhouse getById(Long id) {
        return greenhouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greenhouse not found: " + id));
    }

    public Greenhouse update(Long id, GreenhouseDTO dto) {
        Greenhouse greenhouse = getById(id);
        greenhouse.setName(dto.getName());
        greenhouse.setLocation(dto.getLocation());
        greenhouse.setTriggerTemperature(dto.getTriggerTemperature());
        greenhouse.setTriggerHumidity(dto.getTriggerHumidity());
        return greenhouseRepository.save(greenhouse);
    }

    public void delete(Long id) {
        sensorGrpcClient.deleteSensorsByGreenhouse(id);
        greenhouseRepository.deleteById(id);
    }

    public int deleteByOwnerId(Long ownerId) {
        List<Greenhouse> greenhouses = greenhouseRepository.findByOwnerId(ownerId);
        for (Greenhouse g : greenhouses) {
            sensorGrpcClient.deleteSensorsByGreenhouse(g.getId()); // cascada a sensores
        }
        greenhouseRepository.deleteAll(greenhouses);
        return greenhouses.size();
    }

    public List<Long> getIdsByOwnerId(Long ownerId) {
        return greenhouseRepository.findByOwnerId(ownerId)
                .stream().map(Greenhouse::getId).toList();
    }
}