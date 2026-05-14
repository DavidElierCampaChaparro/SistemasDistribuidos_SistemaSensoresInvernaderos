package com.greenhouse.ingestion_service.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenhouse.ingestion_service.dto.ParsedData;
import com.greenhouse.ingestion_service.model.Format;
import org.springframework.stereotype.Component;

@Component
public class BrandAX100JsonParser implements SensorParser {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Format getSupportedFormat() { return Format.BRAND_A_X100_JSON; }

    @Override
    public ParsedData parse(String rawData) {
        try {
            JsonNode node = mapper.readTree(rawData);
            // Sends: {"temp_f": 77.0, "hum": 0.65}
            float celsius = (node.get("temp_f").floatValue() - 32) * 5 / 9;
            float humidity = node.get("hum").floatValue() * 100;
            return new ParsedData(celsius, humidity);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Brand A X100 JSON", e);
        }
    }
}