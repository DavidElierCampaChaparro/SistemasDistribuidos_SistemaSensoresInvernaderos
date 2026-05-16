package com.greenhouse.auth.endpoint;

import com.greenhouse.auth.model.*;
import com.greenhouse.auth.service.AuthService;
import com.greenhouse.common.util.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class AuthEndpoint {

    private static final String NAMESPACE_URI = "http://auth-service.dev/soap/auth";

    private final AuthService authService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginRequest")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest request) {
        LoginResponse response = new LoginResponse();
        try {
            Owner owner = authService.login(request.getEmail(), request.getPassword());
            response.setSuccess(true);
            response.setMessage("Authentication successful");
            response.setToken(JwtTokenService.generateToken(owner.getEmail(), owner.getId()));
        } catch (RuntimeException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setToken(null);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegisterRequest")
    @ResponsePayload
    public RegisterResponse register(@RequestPayload RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();
        try {
            authService.register(
                    request.getName(),
                    request.getLastname(),
                    request.getEmail(),
                    request.getPassword()
            );
            response.setSuccess(true);
            response.setMessage("Owner registered successfully");
        } catch (RuntimeException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}