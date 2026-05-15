package com.greenhouse.notification_service_test.grpc;

import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseTestServiceGrpc;
import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseOwnerRequest;
import com.greenhouse.grpc.greenhousetesttemporal.GreenhouseOwnerResponse;
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
        GreenhouseOwnerResponse response = greenhouseStub.getGreenhouseOwner(request);
        return response.getOwnerId();
    }
}