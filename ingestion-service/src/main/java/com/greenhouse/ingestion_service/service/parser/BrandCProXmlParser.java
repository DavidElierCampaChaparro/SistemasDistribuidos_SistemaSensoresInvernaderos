package com.greenhouse.ingestion_service.service.parser;

import com.greenhouse.common.enums.Format;
import com.greenhouse.ingestion_service.dto.ParsedData;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@Component
public class BrandCProXmlParser implements SensorParser {

    @Override
    public Format getSupportedFormat() { return Format.BRAND_C_PRO_XML; }

    @Override
    public ParsedData parse(String rawData) {
        try {
            // Sends: <reading><celsius>22.5</celsius><humidity>58.0</humidity></reading>
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(rawData.getBytes()));
            float temperature = Float.parseFloat(doc.getElementsByTagName("celsius").item(0).getTextContent());
            float humidity = Float.parseFloat(doc.getElementsByTagName("humidity").item(0).getTextContent());
            return new ParsedData(temperature, humidity);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Brand C XML", e);
        }
    }
}