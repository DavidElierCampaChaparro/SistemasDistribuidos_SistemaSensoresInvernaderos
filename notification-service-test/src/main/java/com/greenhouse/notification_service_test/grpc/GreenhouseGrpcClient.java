package com.greenhouse.notification_service_test.grpc;

import com.greenhouse.grpc.greenhousetesttemporal.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GreenhouseGrpcClient {

    @GrpcClient("greenhouse-service")
    private GreenhouseTestServiceGrpc.GreenhouseTestServiceBlockingStub greenhouseStub;

    public Long getOwnerId(Long greenhouseId) {
        GreenhouseOwnerRequest request = GreenhouseOwnerRequest.newBuilder()
                .setGreenhouseId(greenhouseId)
                .build();
        return greenhouseStub.getGreenhouseOwner(request).getOwnerId();
    }

    public GreenhouseThresholdResponse getThresholds(Long greenhouseId) {
        GreenhouseThresholdRequest request = GreenhouseThresholdRequest.newBuilder()
                .setGreenhouseId(greenhouseId)
                .build();
        return greenhouseStub.getThresholds(request);
    }
}