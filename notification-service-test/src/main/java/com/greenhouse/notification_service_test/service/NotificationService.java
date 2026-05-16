package com.greenhouse.notification_service_test.service;

import com.greenhouse.common.event.SensorDataEvent;
import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseThresholdResponse;
import com.greenhouse.notification_service_test.grpc.AuthGrpcClient;
import com.greenhouse.notification_service_test.grpc.GreenhouseGrpcClient;
import com.greenhouse.notification_service_test.model.NotificationLog;
import com.greenhouse.notification_service_test.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AuthGrpcClient authGrpcClient;
    private final GreenhouseGrpcClient greenhouseGrpcClient;
    private final NotificationLogRepository notificationLogRepository;
    private final JavaMailSender mailSender;

    public void processNotification(SensorDataEvent event) {
        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setSensorSerialNumber(event.getSensorSerialNumber());
        notificationLog.setGreenhouseId(event.getGreenhouseId());
        notificationLog.setSentAt(LocalDateTime.now());

        GreenhouseThresholdResponse thresholds =
                greenhouseGrpcClient.getThresholds(event.getGreenhouseId());

        boolean tempExceeded = event.getTemperature() > thresholds.getTriggerTemperature();
        boolean humidityExceeded = event.getHumidity() > thresholds.getTriggerHumidity();

        notificationLog.setTemperatureExceeded(tempExceeded);
        notificationLog.setHumidityExceeded(humidityExceeded);

        if (!tempExceeded && !humidityExceeded) {
            notificationLog.setStatus("NO_ALERT");
            notificationLogRepository.save(notificationLog);
            return;
        }

        try {
            Long ownerId = greenhouseGrpcClient.getOwnerId(event.getGreenhouseId());
            String email = authGrpcClient.getOwnerEmail(ownerId);

            notificationLog.setOwnerId(ownerId);
            notificationLog.setEmailSentTo(email);

            sendEmail(email, event, tempExceeded, humidityExceeded);
            notificationLog.setStatus("SENT");
        } catch (Exception e) {
            notificationLog.setStatus("FAILED");
            log.error("Failed to send notification: {}", e.getMessage());
        } finally {
            notificationLogRepository.save(notificationLog);
        }
    }

    private void sendEmail(
            String to,
            SensorDataEvent event,
            boolean tempExceeded,
            boolean humidityExceeded
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Greenhouse Monitoring System - Threshold Alert");

        StringBuilder body = new StringBuilder();
        body.append("Hello,\n\n");
        body.append("The following thresholds have been exceeded in greenhouse ")
                .append(event.getGreenhouseId()).append(":\n\n");

        if (tempExceeded)
            body.append("- Temperature: ").append(event.getTemperature()).append("°C\n");
        if (humidityExceeded)
            body.append("- Humidity: ").append(event.getHumidity()).append("%\n");

        body.append("\nSensor: ").append(event.getSensorSerialNumber());
        body.append("\n\nPlease take the necessary measures.");
        body.append("\n\nGreenhouse Monitoring System");

        message.setText(body.toString());
        mailSender.send(message);
    }
}