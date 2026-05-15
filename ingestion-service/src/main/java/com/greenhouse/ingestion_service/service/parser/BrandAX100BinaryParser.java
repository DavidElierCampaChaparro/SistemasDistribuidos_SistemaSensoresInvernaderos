package com.greenhouse.ingestion_service.service.parser;

import com.greenhouse.common.enums.Format;
import com.greenhouse.ingestion_service.dto.ParsedData;
import org.springframework.stereotype.Component;
import java.nio.ByteBuffer;
import java.util.Base64;

@Component
public class BrandAX100BinaryParser implements SensorParser {

    @Override
    public Format getSupportedFormat() { return Format.BRAND_A_X100_BINARY; }

    @Override
    public ParsedData parse(String rawData) {
        // Arrives as Base64, first 4 bytes = temp in Celsius, next 4 = humidity
        byte[] bytes = Base64.getDecoder().decode(rawData);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        float temperature = buffer.getFloat();
        float humidity = buffer.getFloat();
        return new ParsedData(temperature, humidity);
    }
}