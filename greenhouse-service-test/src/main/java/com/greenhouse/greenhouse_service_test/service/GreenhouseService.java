package com.greenhouse.greenhouse_service_test.service;

import com.greenhouse.greenhouse_service_test.dto.GreenhouseDTO;
import com.greenhouse.greenhouse_service_test.model.Greenhouse;
import com.greenhouse.greenhouse_service_test.repository.GreenhouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GreenhouseService {

    private final GreenhouseRepository greenhouseRepository;

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
        greenhouseRepository.deleteById(id);
    }


}