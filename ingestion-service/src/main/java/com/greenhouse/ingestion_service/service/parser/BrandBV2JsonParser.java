package com.greenhouse.ingestion_service.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenhouse.ingestion_service.dto.ParsedData;
import com.greenhouse.ingestion_service.model.Format;
import org.springframework.stereotype.Component;

@Component
public class BrandBV2JsonParser implements SensorParser {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Format getSupportedFormat() { return Format.BRAND_B_V2_JSON; }

    @Override
    public ParsedData parse(String rawData) {
        try {
            JsonNode node = mapper.readTree(rawData);
            // Sends: {"temperature": 22.5, "relative_humidity": 58.0}
            float temperature = node.get("temperature").floatValue();
            float humidity = node.get("relative_humidity").floatValue();
            return new ParsedData(temperature, humidity);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Brand B V2 JSON", e);
        }
    }
}