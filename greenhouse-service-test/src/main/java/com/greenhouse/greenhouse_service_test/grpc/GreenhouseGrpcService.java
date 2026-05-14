package com.greenhouse.greenhouse_service_test.grpc;

import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseTestServiceGrpc;
import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseThresholdRequest;
import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseThresholdResponse;
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
}