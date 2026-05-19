package com.greenhouse.auth.service;

import com.greenhouse.auth.model.Owner;
import com.greenhouse.auth.repository.OwnerRepository;
import com.greenhouse.auth.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OwnerRepository ownerRepository;

    public Owner register(String name, String lastname, String email, String password) {
        if (ownerRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered: " + email);
        }
        Owner owner = new Owner();
        owner.setName(name);
        owner.setLastname(lastname);
        owner.setEmail(email);
        owner.setPassword(PasswordUtil.encode(password));
        return ownerRepository.save(owner);
    }

    public Owner login(String email, String password) {
        System.out.println("Attempting login for email: " + email);
        return ownerRepository.findByEmail(email)
                .filter(owner -> PasswordUtil.matches(password, owner.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    public Owner getById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found: " + id));
    }
}