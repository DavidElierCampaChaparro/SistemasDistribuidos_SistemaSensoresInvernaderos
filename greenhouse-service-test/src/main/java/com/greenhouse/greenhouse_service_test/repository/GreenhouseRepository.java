package com.greenhouse.greenhouse_service_test.repository;

import com.greenhouse.greenhouse_service_test.model.Greenhouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GreenhouseRepository extends JpaRepository<Greenhouse, Long> {
    List<Greenhouse> findByOwnerId(Long ownerId);
}