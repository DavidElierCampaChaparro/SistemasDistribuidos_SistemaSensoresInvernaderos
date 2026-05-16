package com.greenhouse.notification_service_test.service;

import com.greenhouse.common.event.NotificationEvent;
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

    public void processNotification(NotificationEvent event) {
        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setSensorSerialNumber(event.getSensorSerialNumber());
        notificationLog.setGreenhouseId(event.getGreenhouseId());
        notificationLog.setTemperatureExceeded(event.isTemperatureExceeded());
        notificationLog.setHumidityExceeded(event.isHumidityExceeded());
        notificationLog.setSentAt(LocalDateTime.now());

        try {
            log.info("Getting ownerId for greenhouseId: {}", event.getGreenhouseId());
            Long ownerId = greenhouseGrpcClient.getOwnerId(event.getGreenhouseId());
            log.info("Got ownerId: {}", ownerId);

            String email = authGrpcClient.getOwnerEmail(ownerId);
            log.info("Got email: {}", email);

            notificationLog.setOwnerId(ownerId);
            notificationLog.setEmailSentTo(email);

            sendEmail(email, event);

            notificationLog.setStatus("SENT");
        } catch (Exception e) {
            notificationLog.setStatus("FAILED");
            log.error("Failed to send notification: {}", e.getMessage());
        } finally {
            notificationLogRepository.save(notificationLog);
        }
    }

    private void sendEmail(String to, NotificationEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Greenhouse Monitoring System - Threshold Alert");

        StringBuilder body = new StringBuilder();
        body.append("Hello,\n\n");
        body.append("The following thresholds have been exceeded in greenhouse ")
                .append(event.getGreenhouseId()).append(":\n\n");

        if (event.isTemperatureExceeded()) {
            body.append("- Temperature: ").append(event.getTemperature()).append("°C\n");
        }
        if (event.isHumidityExceeded()) {
            body.append("- Humidity: ").append(event.getHumidity()).append("%\n");
        }

        body.append("\nSensor: ").append(event.getSensorSerialNumber());
        body.append("\n\nPlease take the necessary measures.");
        body.append("\n\nGreenhouse Monitoring System");

        message.setText(body.toString());
        mailSender.send(message);
    }
}