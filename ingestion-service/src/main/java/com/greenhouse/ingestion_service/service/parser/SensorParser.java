package com.greenhouse.ingestion_service.service.parser;

import com.greenhouse.common.enums.Format;
import com.greenhouse.ingestion_service.dto.ParsedData;

public interface SensorParser {
    Format getSupportedFormat();
    ParsedData parse(String rawData);
}