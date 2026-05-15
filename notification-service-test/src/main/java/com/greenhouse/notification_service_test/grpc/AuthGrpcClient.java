package com.greenhouse.notification_service_test.grpc;

import com.greenhouse.grpc.authtesttemporal.AuthTestServiceGrpc;
import com.greenhouse.grpc.authtesttemporal.OwnerRequest;
import com.greenhouse.grpc.authtesttemporal.OwnerEmailResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class AuthGrpcClient {

    @GrpcClient("auth-service")
    private AuthTestServiceGrpc.AuthTestServiceBlockingStub authStub;

    public String getOwnerEmail(Long ownerId) {
        OwnerRequest request = OwnerRequest.newBuilder()
                .setOwnerId(ownerId)
                .build();
        OwnerEmailResponse response = authStub.getOwnerEmail(request);
        return response.getEmail();
    }
}