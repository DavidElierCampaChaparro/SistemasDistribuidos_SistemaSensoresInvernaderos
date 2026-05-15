package com.greenhouse.auth.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class JwtTokenService {

    private static final String SECRET = "auth-service-jwt-secret";
    private static final long EXPIRATION_SECONDS = 3600L;

    private JwtTokenService() {
    }

    public static String generateToken(String username) {
        long issuedAt = Instant.now().getEpochSecond();
        long expiresAt = issuedAt + EXPIRATION_SECONDS;

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = "{\"sub\":\"" + escapeJson(username) + "\",\"username\":\"" + escapeJson(username) + "\",\"iat\":" + issuedAt + ",\"exp\":" + expiresAt + "}";

        String header = base64Url(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signingInput = header + "." + payload;
        String signature = base64Url(hmacSha256(signingInput));
        return signingInput + "." + signature;
    }

    private static byte[] hmacSha256(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to generate JWT token", exception);
        }
    }

    private static String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}