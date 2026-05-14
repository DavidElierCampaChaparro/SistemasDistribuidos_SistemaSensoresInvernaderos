package com.greenhouse.ingestion_service.service.parser;

import com.greenhouse.ingestion_service.dto.ParsedData;
import com.greenhouse.ingestion_service.model.Format;

public interface SensorParser {
    Format getSupportedFormat();
    ParsedData parse(String rawData);
}