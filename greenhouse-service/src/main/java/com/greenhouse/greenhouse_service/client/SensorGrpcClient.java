
package com.greenhouse.greenhouse_service.client;

import com.greenhouse.grpc.sensor.DeleteSensorsByGreenhouseRequest;
import com.greenhouse.grpc.sensor.DeleteSensorsByGreenhouseResponse;
import com.greenhouse.grpc.sensor.SensorServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SensorGrpcClient {

    @GrpcClient("sensor-service-test")
    private SensorServiceGrpc.SensorServiceBlockingStub sensorStub;

    public int deleteSensorsByGreenhouse(Long greenhouseId) {
        DeleteSensorsByGreenhouseRequest request = DeleteSensorsByGreenhouseRequest.newBuilder()
                .setGreenhouseId(greenhouseId)
                .build();
        DeleteSensorsByGreenhouseResponse response = sensorStub.deleteSensorsByGreenhouse(request);
        return response.getDeletedCount();
    }
}