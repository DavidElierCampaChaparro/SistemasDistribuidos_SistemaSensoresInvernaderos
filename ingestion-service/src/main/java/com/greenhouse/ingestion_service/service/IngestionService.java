package com.greenhouse.ingestion_service.service;

import com.greenhouse.ingestion_service.dto.SensorDataDTO;
import com.greenhouse.ingestion_service.model.Record;
import com.greenhouse.ingestion_service.repository.RecordRepository;
import com.greenhouse.ingestion_service.dto.ParsedData;
import com.greenhouse.ingestion_service.service.parser.SensorParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final RecordRepository recordRepository;
    private final List<SensorParser> parsers;

    public Record save(SensorDataDTO dto) {
        SensorParser parser = parsers.stream()
                .filter(p -> p.getSupportedFormat().equals(dto.getFormat()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Non-supported format: " + dto.getFormat()));

        ParsedData data = parser.parse(dto.getRawData());

        Record record = new Record();
        record.setSensorSerialNumber(dto.getSensorSerialNumber());
        record.setTemperature(data.getTemperature());
        record.setHumidity(data.getHumidity());
        record.setTimestamp(LocalDateTime.now());
        return recordRepository.save(record);
    }
}