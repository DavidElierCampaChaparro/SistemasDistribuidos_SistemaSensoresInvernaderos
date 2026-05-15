package com.greenhouse.auth.service;

import com.greenhouse.auth.model.Owner;
import com.greenhouse.auth.repo.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OwnerRepository ownerRepository;

    public Owner login(String email, String password) {
        return ownerRepository.findByEmail(email)
                .filter(owner -> owner.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    public Owner getById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found: " + id));
    }
}