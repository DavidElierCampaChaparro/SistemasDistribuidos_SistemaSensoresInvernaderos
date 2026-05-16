package com.greenhouse.analytics_service_test.controller;

import com.greenhouse.analytics_service_test.model.SensorRecord;
import com.greenhouse.analytics_service_test.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analytics/xml")
@RequiredArgsConstructor
public class AnalyticsXmlController {

    private final AnalyticsService analyticsService;

    @GetMapping(value = "/greenhouse/{greenhouseId}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getGreenhouseReportXml(
            @PathVariable Long greenhouseId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) throws Exception {

        List<SensorRecord> records = analyticsService.getByGreenhouse(greenhouseId, from, to);

        String xml = buildXml(greenhouseId, from, to, records);
        validateXml(xml);

        return ResponseEntity.ok(xml);
    }

    private String buildXml(Long greenhouseId, LocalDateTime from, LocalDateTime to,
                            List<SensorRecord> records) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<sensorDataReport>\n");
        sb.append("  <greenhouseId>").append(greenhouseId).append("</greenhouseId>\n");
        sb.append("  <from>").append(from).append("</from>\n");
        sb.append("  <to>").append(to).append("</to>\n");
        sb.append("  <records>\n");

        for (SensorRecord r : records) {
            sb.append("    <record>\n");
            sb.append("      <sensorSerialNumber>").append(r.getSensorSerialNumber()).append("</sensorSerialNumber>\n");
            sb.append("      <temperature>").append(r.getTemperature()).append("</temperature>\n");
            sb.append("      <humidity>").append(r.getHumidity()).append("</humidity>\n");
            sb.append("      <timestamp>").append(r.getTimestamp()).append("</timestamp>\n");
            sb.append("    </record>\n");
        }

        sb.append("  </records>\n");
        sb.append("</sensorDataReport>");
        return sb.toString();
    }

    private void validateXml(String xml) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Resource resource = new ClassPathResource("xsd/sensor-data.xsd");
        Schema schema = factory.newSchema(resource.getURL());

        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xml)));
    }

    @ExceptionHandler(SAXException.class)
    public ResponseEntity<String> handleValidationError(SAXException e) {
        return ResponseEntity.badRequest().body("XML validation failed: " + e.getMessage());
    }
}