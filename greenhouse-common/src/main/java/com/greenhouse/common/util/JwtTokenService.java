package com.greenhouse.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public final class JwtTokenService {

    private static final String SECRET = "auth-service-jwt-secret";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    private JwtTokenService() {}

    public static String generateToken(String email, Long userId) {
        return JWT.create()
                .withSubject(email)
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(ALGORITHM);
    }

    public static DecodedJWT validateToken(String token) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(token);
    }
}