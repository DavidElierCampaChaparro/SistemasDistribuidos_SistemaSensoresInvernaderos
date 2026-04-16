package com.greenhouse.sensormanagementservice.mapper;

import com.greenhouse.sensormanagementservice.dto.SensorRequestDTO;
import com.greenhouse.sensormanagementservice.dto.SensorResponseDTO;
import com.greenhouse.sensormanagementservice.entity.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(source = "format.id", target = "formatId")
    SensorResponseDTO toResponseDto(Sensor sensor);

    @Mapping(target = "format", ignore = true)
    Sensor toEntity(SensorRequestDTO sensorRequestDTO);

}