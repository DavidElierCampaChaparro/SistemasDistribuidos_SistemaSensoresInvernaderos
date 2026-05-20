package com.greenhouse.auth.service;

import com.greenhouse.auth.model.Owner;
import com.greenhouse.auth.repository.OwnerRepository;
import com.greenhouse.auth.client.GreenhouseGrpcClient;

import com.greenhouse.auth.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OwnerRepository ownerRepository;
    private final GreenhouseGrpcClient greenhouseGrpcClient;

    public Owner register(String name, String lastname, String email, String password) {
        if (ownerRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered: " + email);
        }
        Owner owner = new Owner();
        owner.setName(name);
        owner.setLastname(lastname);
        owner.setEmail(email);
        owner.setPassword(password);
        return ownerRepository.save(owner);
    }

    public Owner login(String email, String password) {
        return ownerRepository.findByEmail(email)
                .filter(o -> password.equals(o.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    public Owner getById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found: " + id));
    }

    public Owner update(Long id, String name, String lastname, String email) {
        Owner owner = getById(id);
        owner.setName(name);
        owner.setLastname(lastname);
        owner.setEmail(email);
        return ownerRepository.save(owner);
    }

    public void delete(Long id) {
        getById(id);
        greenhouseGrpcClient.deleteGreenhousesByOwner(id);
        ownerRepository.deleteById(id);
    }
}