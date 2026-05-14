package com.greenhouse.ingestion_service.client;

import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseTestServiceGrpc;
import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseThresholdRequest;
import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseThresholdResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GreenhouseGrpcClient {

    @GrpcClient("greenhouse-service")
    private GreenhouseTestServiceGrpc.GreenhouseTestServiceBlockingStub greenhouseStub;

    public GreenhouseThresholdResponse getThresholds(Long greenhouseId) {
        GreenhouseThresholdRequest request = GreenhouseThresholdRequest.newBuilder()
                .setGreenhouseId(greenhouseId)
                .build();
        return greenhouseStub.getThresholds(request);
    }
}