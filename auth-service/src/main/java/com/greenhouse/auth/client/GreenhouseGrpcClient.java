
package com.greenhouse.auth.client;

import com.greenhouse.grpc.greenhouse.DeleteGreenhousesByOwnerRequest;
import com.greenhouse.grpc.greenhouse.GreenhouseServiceGrpc;
import com.greenhouse.grpc.greenhouse.GreenhousesByOwnerRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GreenhouseGrpcClient {

    @GrpcClient("greenhouse-service")
    private GreenhouseServiceGrpc.GreenhouseServiceBlockingStub greenhouseStub;

    public int deleteGreenhousesByOwner(Long ownerId) {
        DeleteGreenhousesByOwnerRequest request = DeleteGreenhousesByOwnerRequest.newBuilder()
                .setOwnerId(ownerId)
                .build();
        return greenhouseStub.deleteGreenhousesByOwner(request).getDeletedCount();
    }

    public List<Long> getGreenhousesByOwner(Long ownerId) {
        GreenhousesByOwnerRequest request = GreenhousesByOwnerRequest.newBuilder()
                .setOwnerId(ownerId)
                .build();
        return greenhouseStub.getGreenhousesByOwner(request).getGreenhouseIdsList();
    }
}