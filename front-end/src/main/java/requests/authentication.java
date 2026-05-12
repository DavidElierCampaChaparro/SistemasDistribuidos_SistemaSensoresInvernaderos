package requests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class authentication {

    private static final String SERVICE_URL = "http://localhost:8080/ws";
    private static final String NAMESPACE = "http://auth-service.dev/soap/auth";
    private static volatile String lastToken;

    private authentication() {
    }

    public static AuthResponse login(String username, String password) throws Exception {
        String requestBody = buildRequest(username, password);
        HttpURLConnection connection = (HttpURLConnection) new URL(SERVICE_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.setRequestProperty("Accept", "text/xml");

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        InputStream inputStream = connection.getResponseCode() >= 400
                ? connection.getErrorStream()
                : connection.getInputStream();

        if (inputStream == null) {
            throw new IOException("No response returned by auth-service");
        }

        String responseXml;
        try (InputStream stream = inputStream; ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            stream.transferTo(buffer);
            responseXml = buffer.toString(StandardCharsets.UTF_8);
        }

        if (connection.getResponseCode() >= 400) {
            throw new IOException("Auth service returned HTTP " + connection.getResponseCode() + ": " + responseXml);
        }

        AuthResponse response = parseResponse(responseXml);
        lastToken = response.getToken();
        return response;
    }

    public static String getLastToken() {
        return lastToken;
    }

    private static String buildRequest(String username, String password) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\""
                + " xmlns:auth=\"" + NAMESPACE + "\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<auth:LoginRequest>"
                + "<auth:username>" + escapeXml(username) + "</auth:username>"
                + "<auth:password>" + escapeXml(password) + "</auth:password>"
                + "</auth:LoginRequest>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
    }

    private static AuthResponse parseResponse(String responseXml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(responseXml.getBytes(StandardCharsets.UTF_8)));
        NodeList faultList = document.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Fault");
        if (faultList.getLength() > 0) {
            throw new IOException("SOAP fault returned by auth-service");
        }

        NodeList responseNodes = document.getElementsByTagNameNS(NAMESPACE, "LoginResponse");
        if (responseNodes.getLength() == 0) {
            throw new IOException("LoginResponse not found in auth-service response");
        }

        Element responseElement = (Element) responseNodes.item(0);
        AuthResponse response = new AuthResponse();
        response.setSuccess(getBooleanChild(responseElement, "success"));
        response.setMessage(getTextChild(responseElement, "message"));
        response.setToken(getTextChild(responseElement, "token"));
        return response;
    }

    private static boolean getBooleanChild(Element parent, String childName) {
        String value = getTextChild(parent, childName);
        return value != null && Boolean.parseBoolean(value);
    }

    private static String getTextChild(Element parent, String childName) {
        NodeList nodes = parent.getElementsByTagNameNS(NAMESPACE, childName);
        if (nodes.getLength() == 0 || nodes.item(0) == null) {
            return null;
        }
        String text = nodes.item(0).getTextContent();
        return text == null || text.isBlank() ? null : text.trim();
    }

    private static String escapeXml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
