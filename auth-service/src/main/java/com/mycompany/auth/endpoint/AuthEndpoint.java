package com.mycompany.auth.endpoint;

import com.mycompany.auth.model.LoginRequest;
import com.mycompany.auth.model.LoginResponse;
import com.mycompany.auth.service.AuthService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AuthEndpoint {

    private static final String NAMESPACE_URI = "http://auth-service.dev/soap/auth";

    private final AuthService authService;

    public AuthEndpoint(AuthService authService) {
        this.authService = authService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginRequest")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }
}
