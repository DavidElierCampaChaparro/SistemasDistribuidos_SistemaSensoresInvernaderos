package com.greenhouse.api_gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.greenhouse.common.util.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@Component
public class JwtAuthFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String token = authHeader.substring(7);

        try {
            JwtTokenService.validateToken(token);
        } catch (JWTVerificationException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        return next.handle(request);
    }
}