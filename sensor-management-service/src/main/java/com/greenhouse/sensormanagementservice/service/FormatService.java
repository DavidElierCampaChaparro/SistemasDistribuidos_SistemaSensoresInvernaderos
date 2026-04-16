package com.greenhouse.sensormanagementservice.service;

import com.greenhouse.sensormanagementservice.entity.Format;
import com.greenhouse.sensormanagementservice.repository.FormatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormatService {

    private final FormatRepository formatRepository;

    public Format saveFormat(String name) {
        Format format = new Format();
        format.setName(name);
        return formatRepository.save(format);
    }
}