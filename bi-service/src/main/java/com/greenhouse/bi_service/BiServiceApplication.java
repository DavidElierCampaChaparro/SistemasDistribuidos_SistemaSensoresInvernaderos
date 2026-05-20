import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringReader;
import java.time.LocalDateTime;

@Service
public class BiXmlConsumerService {

    private final RestTemplate restTemplate;
    private final String ANALYTICS_URL = "http://localhost:8080/api/analytics/xml/greenhouse/";
    
    private final String XSD_PATH = "src/main/resources/xsd/sensor-data.xsd"; 

    public BiXmlConsumerService() {
        this.restTemplate = new RestTemplate();
    }

    public void processAnalyticsReport(Long greenhouseId, LocalDateTime from, LocalDateTime to) {
        try {
            String url = ANALYTICS_URL + greenhouseId + "?from=" + from + "&to=" + to;
            String xmlResponse = restTemplate.getForObject(url, String.class);

            if (xmlResponse == null || xmlResponse.isEmpty()) {
                System.out.println("No se recibieron datos.");
                return;
            }

            validateXmlAgainstXsd(xmlResponse);
            System.out.println("✅ El XML recibido cumple con el esquema XSD.");

            analyzeData(xmlResponse);

        } catch (Exception e) {
            System.err.println("❌ Error consumiendo el servicio de BI: " + e.getMessage());
        }
    }

    private void validateXmlAgainstXsd(String xml) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(XSD_PATH));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xml)));
    }

    private void analyzeData(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        
        document.getDocumentElement().normalize();

        NodeList temperatureNodes = document.getElementsByTagName("temperature");
        NodeList humidityNodes = document.getElementsByTagName("humidity");

        int totalRecords = temperatureNodes.getLength();
        if (totalRecords == 0) {
            System.out.println("No hay registros en este periodo.");
            return;
        }

        double sumTemperature = 0;
        double sumHumidity = 0;

        for (int i = 0; i < totalRecords; i++) {
            sumTemperature += Double.parseDouble(temperatureNodes.item(i).getTextContent());
            sumHumidity += Double.parseDouble(humidityNodes.item(i).getTextContent());
        }

        System.out.println("========== REPORTE BI ==========");
        System.out.println("Total de lecturas analizadas: " + totalRecords);
        System.out.println("Temperatura Promedio: " + (sumTemperature / totalRecords) + " °C");
        System.out.println("Humedad Promedio: " + (sumHumidity / totalRecords) + " %");
        System.out.println("================================");
    }
}