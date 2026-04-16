package com.greenhouse.sensormanagementservice.repository;

import com.greenhouse.sensormanagementservice.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormatRepository extends JpaRepository<Format, Long> {

}