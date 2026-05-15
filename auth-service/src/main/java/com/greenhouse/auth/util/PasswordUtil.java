package com.greenhouse.auth.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    public static String encode(String password) {
        try {
            // Random salt generation:
            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            String salt = Base64.getEncoder().encodeToString(saltBytes);

            // Hashing of password and salt:
            String salted = password + salt;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(salted.getBytes());
            String hashedPassword = Base64.getEncoder().encodeToString(hash);

            // Hash and salt are saved together:
            return salt + ":" + hashedPassword;
        } catch (Exception e) {
            throw new RuntimeException("Error encoding password", e);
        }
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        try {
            // Separation of hash and salt:
            String[] parts = encodedPassword.split(":");
            String salt = parts[0];
            String storedHash = parts[1];

            // Hashing received password with the same salt:
            String salted = rawPassword + salt;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(salted.getBytes());
            String hashedInput = Base64.getEncoder().encodeToString(hash);

            return hashedInput.equals(storedHash);
        } catch (Exception e) {
            throw new RuntimeException("Error matching password", e);
        }
    }
}