package com.greenhouse.notification_service_test.consumer;

import com.greenhouse.common.event.SensorDataEvent;
import com.greenhouse.notification_service_test.config.RabbitMQConfig;
import com.greenhouse.notification_service_test.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(SensorDataEvent event) {
        log.info("Received notification event for sensor: {}", event.getSensorSerialNumber());
        notificationService.processNotification(event);
    }
}