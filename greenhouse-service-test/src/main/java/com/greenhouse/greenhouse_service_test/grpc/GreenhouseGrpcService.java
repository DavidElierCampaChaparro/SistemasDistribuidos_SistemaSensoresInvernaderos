package com.greenhouse.greenhouse_service_test.grpc;

import com.greenhouse.greenhouse_service_test.model.Greenhouse;
import com.greenhouse.grpc.greenhousetesttemporal.*;
import com.greenhouse.greenhouse_service_test.service.GreenhouseService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GreenhouseGrpcService extends GreenhouseTestServiceGrpc.GreenhouseTestServiceImplBase {

    private final GreenhouseService greenhouseService;

    @Override
    public void getThresholds(GreenhouseThresholdRequest request, StreamObserver<GreenhouseThresholdResponse> responseObserver) {
        var greenhouse = greenhouseService.getById(request.getGreenhouseId());

        GreenhouseThresholdResponse response = GreenhouseThresholdResponse.newBuilder()
                .setTriggerTemperature(greenhouse.getTriggerTemperature())
                .setTriggerHumidity(greenhouse.getTriggerHumidity())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getGreenhouseOwner(GreenhouseOwnerRequest request, StreamObserver<GreenhouseOwnerResponse> responseObserver) {
        Greenhouse greenhouse = greenhouseService.getById(request.getGreenhouseId());

        GreenhouseOwnerResponse response = GreenhouseOwnerResponse.newBuilder()
                .setOwnerId(greenhouse.getOwnerId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}