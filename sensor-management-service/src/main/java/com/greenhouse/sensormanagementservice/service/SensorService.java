package com.greenhouse.sensormanagementservice.service;

import com.greenhouse.sensormanagementservice.client.GreenhouseGrpcClient;
import com.greenhouse.sensormanagementservice.dto.SensorRequestDTO;
import com.greenhouse.sensormanagementservice.dto.SensorResponseDTO;
import com.greenhouse.sensormanagementservice.entity.Format;
import com.greenhouse.sensormanagementservice.entity.Sensor;
import com.greenhouse.sensormanagementservice.exception.BusinessException;
import com.greenhouse.sensormanagementservice.mapper.SensorMapper;
import com.greenhouse.sensormanagementservice.repository.FormatRepository;
import com.greenhouse.sensormanagementservice.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final FormatRepository formatRepository;
    private final SensorMapper sensorMapper;
    private final GreenhouseGrpcClient greenhouseGrpcClient;

    private final String MESSAGE_FORMAT_NOT_FOUND = "Format not found. The sensor could not be saved.";
    private final String MESSAGE_GREENHOUSE_NOT_FOUND = "Greenhouse not found. The sensor could not be saved.";

    public SensorResponseDTO saveSensor(SensorRequestDTO sensorRequestDTO){

        // Format validation
        Format format = formatRepository
                .findById(sensorRequestDTO.getFormatId())
                .orElseThrow(() -> new BusinessException(MESSAGE_FORMAT_NOT_FOUND));

        // Greenhouse validation
        boolean greenhouseExists = greenhouseGrpcClient.validateGreenhouseExists(sensorRequestDTO.getGreenhouseId());
        if (!greenhouseExists) {
            throw new BusinessException(MESSAGE_GREENHOUSE_NOT_FOUND);
        }

        Sensor sensor = sensorMapper.toEntity(sensorRequestDTO);

        sensor.setFormat(format);

        Sensor sensorSaved = sensorRepository.save(sensor);

        return sensorMapper.toResponseDto(sensorSaved);
    }

}
