package com.mycompany.auth.service;

import com.mycompany.auth.model.LoginResponse;
import com.mycompany.auth.repo.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminRepository adminRepository;

    public AuthService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public LoginResponse login(String username, String password) {
        LoginResponse response = new LoginResponse();

        if (isBlank(username) || isBlank(password)) {
            response.setSuccess(false);
            response.setMessage("username and password are required");
            response.setToken(null);
            return response;
        }

        boolean authenticated = adminRepository.authenticate(username, password);
        response.setSuccess(authenticated);
        response.setMessage(authenticated ? "authentication successful" : "invalid credentials");
        response.setToken(authenticated ? JwtTokenService.generateToken(username) : null);
        return response;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
