package com.greenhouse.ingestion_service.service;

import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseThresholdResponse;
import com.greenhouse.ingestion_service.client.GreenhouseGrpcClient;
import com.greenhouse.ingestion_service.client.SensorGrpcClient;
import com.greenhouse.ingestion_service.dto.SensorDataDTO;
import com.greenhouse.ingestion_service.model.Format;
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
    private final SensorGrpcClient sensorGrpcClient;
    private final GreenhouseGrpcClient greenhouseGrpcClient;

    public Record save(SensorDataDTO dto) {
        Format format = sensorGrpcClient.getFormat(dto.getSensorSerialNumber());

        SensorParser parser = parsers.stream()
                .filter(p -> p.getSupportedFormat().equals(format))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unsupported format: " + format));

        ParsedData data = parser.parse(dto.getRawData());

        Long greenhouseId = sensorGrpcClient.getGreenhouseId(dto.getSensorSerialNumber());
        GreenhouseThresholdResponse thresholds = greenhouseGrpcClient.getThresholds(greenhouseId);

        boolean temperatureExceeded = data.getTemperature() > thresholds.getTriggerTemperature();
        boolean humidityExceeded = data.getHumidity() > thresholds.getTriggerHumidity();

        if (temperatureExceeded || humidityExceeded) {
            // TODO: call NotificationService via gRPC
        }

        Record record = new Record();
        record.setSensorSerialNumber(dto.getSensorSerialNumber());
        record.setTemperature(data.getTemperature());
        record.setHumidity(data.getHumidity());
        record.setTimestamp(LocalDateTime.now());
        return recordRepository.save(record);
    }
}