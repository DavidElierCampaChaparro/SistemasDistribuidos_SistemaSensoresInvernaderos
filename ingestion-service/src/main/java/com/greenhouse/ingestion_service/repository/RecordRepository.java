package com.greenhouse.ingestion_service.repository;

import com.greenhouse.ingestion_service.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {



}