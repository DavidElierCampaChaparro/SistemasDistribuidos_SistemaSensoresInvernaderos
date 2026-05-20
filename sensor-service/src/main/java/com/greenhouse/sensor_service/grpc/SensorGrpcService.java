package com.greenhouse.sensor_service.grpc;

import com.greenhouse.grpc.sensor.SensorFormatResponse;
import com.greenhouse.grpc.sensor.SensorServiceGrpc;
import com.greenhouse.grpc.sensor.*;
import com.greenhouse.sensor_service.service.SensorService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class SensorGrpcService extends SensorServiceGrpc.SensorServiceImplBase {

    private final SensorService sensorService;

    @Override
    public void getSensorFormat(SensorRequest request, StreamObserver<SensorFormatResponse> responseObserver) {
        String format = sensorService.getBySerialNumber(request.getSerialNumber())
                .getFormat().name();
        responseObserver.onNext(SensorFormatResponse.newBuilder().setFormat(format).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getSensorGreenhouseId(SensorRequest request, StreamObserver<SensorGreenhouseResponse> responseObserver) {
        Long greenhouseId = sensorService.getBySerialNumber(request.getSerialNumber())
                .getGreenhouseId();
        responseObserver.onNext(SensorGreenhouseResponse.newBuilder().setGreenhouseId(greenhouseId).build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteSensorsByGreenhouse(DeleteSensorsByGreenhouseRequest request,
                                          StreamObserver<DeleteSensorsByGreenhouseResponse> responseObserver) {
        int deleted = sensorService.deleteByGreenhouseId(request.getGreenhouseId());
        responseObserver.onNext(DeleteSensorsByGreenhouseResponse.newBuilder().setDeletedCount(deleted).build());
        responseObserver.onCompleted();
    }
}