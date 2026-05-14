package com.greenhouse.ingestion_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParsedData {
    private Float temperature;
    private Float humidity;
}