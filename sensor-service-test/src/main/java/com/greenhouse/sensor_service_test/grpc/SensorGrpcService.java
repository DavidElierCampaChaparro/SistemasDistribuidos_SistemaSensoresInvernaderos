package com.greenhouse.sensor_service_test.grpc;

import com.greenhouse.grpc.sensortesttemporal.SensorFormatResponse;
import com.greenhouse.grpc.sensortesttemporal.SensorGreenhouseResponse;
import com.greenhouse.grpc.sensortesttemporal.SensorRequest;
import com.greenhouse.grpc.sensortesttemporal.SensorTestServiceGrpc;
import com.greenhouse.sensor_service_test.service.SensorService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class SensorGrpcService extends SensorTestServiceGrpc.SensorTestServiceImplBase {

    private final SensorService sensorService;

    @Override
    public void getSensorFormat(SensorRequest request, StreamObserver<SensorFormatResponse> responseObserver) {
        String format = sensorService.getBySerialNumber(request.getSerialNumber())
                .getFormat().name();

        SensorFormatResponse response = SensorFormatResponse.newBuilder()
                .setFormat(format)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getSensorGreenhouseId(SensorRequest request, StreamObserver<SensorGreenhouseResponse> responseObserver) {
        Long greenhouseId = sensorService.getBySerialNumber(request.getSerialNumber())
                .getGreenhouseId();

        SensorGreenhouseResponse response = SensorGreenhouseResponse.newBuilder()
                .setGreenhouseId(greenhouseId)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}