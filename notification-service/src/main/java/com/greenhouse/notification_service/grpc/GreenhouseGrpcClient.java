package com.greenhouse.notification_service.grpc;

import com.greenhouse.grpc.greenhouse.GreenhouseOwnerRequest;
import com.greenhouse.grpc.greenhouse.GreenhouseServiceGrpc;
import com.greenhouse.grpc.greenhouse.GreenhouseThresholdRequest;
import com.greenhouse.grpc.greenhouse.GreenhouseThresholdResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GreenhouseGrpcClient {

    @GrpcClient("greenhouse-service")
    private GreenhouseServiceGrpc.GreenhouseServiceBlockingStub greenhouseStub;

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