package com.greenhouse.analytics_service_test.consumer;

import com.greenhouse.analytics_service_test.config.RabbitMQConfig;
import com.greenhouse.analytics_service_test.model.SensorRecord;
import com.greenhouse.analytics_service_test.repository.SensorRecordRepository;
import com.greenhouse.common.event.SensorDataEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsConsumer {

    private final SensorRecordRepository sensorRecordRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void onMessage(SensorDataEvent event) {
        SensorRecord record = new SensorRecord();
        record.setSensorSerialNumber(event.getSensorSerialNumber());
        record.setGreenhouseId(event.getGreenhouseId());
        record.setTemperature(event.getTemperature());
        record.setHumidity(event.getHumidity());
        record.setTimestamp(event.getTimestamp());
        sensorRecordRepository.save(record);
        log.info("Saved record for sensor {}", event.getSensorSerialNumber());
    }
}