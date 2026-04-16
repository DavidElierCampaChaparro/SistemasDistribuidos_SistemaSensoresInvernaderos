package com.greenhouse.sensormanagementservice.client;

import com.greenhouse.grpc.greenhouse.GreenhouseRequest;
import com.greenhouse.grpc.greenhouse.GreenhouseResponse;
import com.greenhouse.grpc.greenhouse.GreenhouseValidationServiceGrpc;
import com.greenhouse.sensormanagementservice.exception.ServiceUnavailableException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;

@Service
public class GreenhouseGrpcClient {

    private static final String GREENHOUSE_HOST = "localhost";
    private static final int GREENHOUSE_GRPC_PORT = 9090;

    private final String GREENHOUSE_SERVICE_UNAVAILABLE = "GreenhouseService is unreachable.";

    public boolean validateGreenhouseExists(Long greenhouseId) {
        // Channel to the other microservice
        ManagedChannel channel = ManagedChannelBuilder.forAddress(GREENHOUSE_HOST, GREENHOUSE_GRPC_PORT)
                .usePlaintext() // Not using HTTPS/SSL during development
                .build();

        try {
            // Creation of Stub. It's synchronous (BlockingStub)
            GreenhouseValidationServiceGrpc.GreenhouseValidationServiceBlockingStub stub =
                    GreenhouseValidationServiceGrpc.newBlockingStub(channel);

            // Creation of binary message
            GreenhouseRequest request = GreenhouseRequest.newBuilder()
                    .setGreenhouseId(greenhouseId)
                    .build();

            // Network call
            GreenhouseResponse response = stub.validateGreenhouse(request);

            // Return response
            return response.getExists();

        } catch (StatusRuntimeException ex) {
            throw new ServiceUnavailableException(GREENHOUSE_SERVICE_UNAVAILABLE);
        } finally {
            channel.shutdown();
        }
    }
}