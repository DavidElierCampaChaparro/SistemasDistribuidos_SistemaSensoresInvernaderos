package com.greenhouse.auth.endpoint;

import com.greenhouse.auth.model.Owner;
import com.greenhouse.auth.repo.OwnerRepository;
import com.greenhouse.grpc.authtesttemporal.AuthTestServiceGrpc;
import com.greenhouse.grpc.authtesttemporal.OwnerEmailResponse;
import com.greenhouse.grpc.authtesttemporal.OwnerRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GrpcAuthEndpoint extends AuthTestServiceGrpc.AuthTestServiceImplBase {

    private final OwnerRepository ownerRepository;

    @Override
    public void getOwnerEmail(OwnerRequest request, StreamObserver<OwnerEmailResponse> responseObserver) {
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found: " + request.getOwnerId()));

        OwnerEmailResponse response = OwnerEmailResponse.newBuilder()
                .setEmail(owner.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}