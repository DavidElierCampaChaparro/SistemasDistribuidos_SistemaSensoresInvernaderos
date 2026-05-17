/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package requests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author crazy
 */
public class ReportRequest {
    private static final String BASE_URL = "http://localhost:8084";

    public ReportResponse request(int greenhouseId) throws IOException {
        String jsonBody = "{\"greenhouse_id\":" + greenhouseId + "}";
        HttpURLConnection connection = openConnection("/reports", "POST", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        String responseBody = readBody(connection);
        if (connection.getResponseCode() >= 400) {
            return ReportResponse.forStatus(null, "error", responseBody);
        }

        return parseStatusResponse(responseBody);
    }

    public ReportResponse getStatus(String jobId) throws IOException {
        HttpURLConnection connection = openConnection("/reports/" + jobId, "GET", "application/json");
        String responseBody = readBody(connection);
        if (connection.getResponseCode() >= 400) {
            return ReportResponse.forStatus(jobId, "error", responseBody);
        }
        return parseStatusResponse(responseBody);
    }

    public ReportResponse downloadPdf(String jobId) throws IOException {
        HttpURLConnection connection = openConnection(
            "/reports/" + jobId + "/download",
            "GET",
            "application/pdf"
        );

        int responseCode = connection.getResponseCode();
        InputStream stream = responseCode >= 400
            ? connection.getErrorStream()
            : connection.getInputStream();

        if (stream == null || responseCode >= 400) {
            String message = stream == null ? "" : readStream(stream);
            return ReportResponse.forStatus(jobId, "error", message);
        }

        byte[] pdfBytes = readBytes(stream);
        return ReportResponse.forPdf(jobId, pdfBytes);
    }

    private HttpURLConnection openConnection(String path, String method, String accept) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + path).openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Accept", accept);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        return connection;
    }

    private ReportResponse parseStatusResponse(String json) {
        String jobId = extractJsonValue(json, "job_id");
        String status = extractJsonValue(json, "status");
        String message = extractJsonValue(json, "message");
        if (status == null || status.isBlank()) {
            status = "unknown";
        }
        return ReportResponse.forStatus(jobId, status, message);
    }

    private String readBody(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getResponseCode() >= 400
            ? connection.getErrorStream()
            : connection.getInputStream();
        if (stream == null) {
            return "";
        }
        return readStream(stream);
    }

    private String readStream(InputStream stream) throws IOException {
        return new String(readBytes(stream), StandardCharsets.UTF_8);
    }

    private byte[] readBytes(InputStream stream) throws IOException {
        try (InputStream input = stream; ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            input.transferTo(buffer);
            return buffer.toByteArray();
        }
    }

    private String extractJsonValue(String json, String key) {
        if (json == null) {
            return null;
        }
        String quotedKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(quotedKey);
        if (keyIndex < 0) {
            return null;
        }
        int colonIndex = json.indexOf(':', keyIndex + quotedKey.length());
        if (colonIndex < 0) {
            return null;
        }
        int start = colonIndex + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }
        if (start >= json.length()) {
            return null;
        }
        char first = json.charAt(start);
        if (first == '"') {
            int end = json.indexOf('"', start + 1);
            while (end > 0 && json.charAt(end - 1) == '\\') {
                end = json.indexOf('"', end + 1);
            }
            if (end < 0) {
                return null;
            }
            return json.substring(start + 1, end);
        }
        int end = start;
        while (end < json.length() && ",}\n\r\t ".indexOf(json.charAt(end)) == -1) {
            end++;
        }
        return json.substring(start, end);
    }
}
