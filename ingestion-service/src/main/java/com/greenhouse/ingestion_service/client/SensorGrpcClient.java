package com.greenhouse.ingestion_service.client;


import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;


import com.greenhouse.grpc.sensortesttemporal.SensorRequest;
import com.greenhouse.grpc.sensortesttemporal.SensorTestServiceGrpc;
import com.greenhouse.ingestion_service.model.Format;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class SensorGrpcClient {

    @GrpcClient("sensor-service")
    private SensorTestServiceGrpc.SensorTestServiceBlockingStub sensorStub;

    public Format getFormat(String serialNumber) {
        SensorRequest request = SensorRequest.newBuilder()
                .setSerialNumber(serialNumber)
                .build();
        return Format.valueOf(sensorStub.getSensorFormat(request).getFormat());
    }

    public Long getGreenhouseId(String serialNumber) {
        SensorRequest request = SensorRequest.newBuilder()
                .setSerialNumber(serialNumber)
                .build();
        return sensorStub.getSensorGreenhouseId(request).getGreenhouseId();
    }
}