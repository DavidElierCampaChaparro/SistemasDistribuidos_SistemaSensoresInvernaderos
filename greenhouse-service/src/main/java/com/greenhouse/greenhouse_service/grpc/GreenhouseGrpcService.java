package com.greenhouse.greenhouse_service.grpc;

import com.greenhouse.greenhouse_service.model.Greenhouse;
import com.greenhouse.grpc.greenhouse.GreenhouseServiceGrpc;
import com.greenhouse.grpc.greenhouse.GreenhouseThresholdRequest;
import com.greenhouse.grpc.greenhouse.GreenhouseThresholdResponse;
import com.greenhouse.grpc.greenhouse.*;
import com.greenhouse.greenhouse_service.service.GreenhouseService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class GreenhouseGrpcService extends GreenhouseServiceGrpc.GreenhouseServiceImplBase {

    private final GreenhouseService greenhouseService;

    @Override
    public void getThresholds(GreenhouseThresholdRequest request,
                              StreamObserver<GreenhouseThresholdResponse> responseObserver) {
        var greenhouse = greenhouseService.getById(request.getGreenhouseId());
        responseObserver.onNext(GreenhouseThresholdResponse.newBuilder()
                .setTriggerTemperature(greenhouse.getTriggerTemperature())
                .setTriggerHumidity(greenhouse.getTriggerHumidity())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getGreenhouseOwner(GreenhouseOwnerRequest request,
                                   StreamObserver<GreenhouseOwnerResponse> responseObserver) {
        Greenhouse greenhouse = greenhouseService.getById(request.getGreenhouseId());
        responseObserver.onNext(GreenhouseOwnerResponse.newBuilder()
                .setOwnerId(greenhouse.getOwnerId())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteGreenhousesByOwner(DeleteGreenhousesByOwnerRequest request,
                                         StreamObserver<DeleteGreenhousesByOwnerResponse> responseObserver) {
        int deleted = greenhouseService.deleteByOwnerId(request.getOwnerId());
        responseObserver.onNext(DeleteGreenhousesByOwnerResponse.newBuilder()
                .setDeletedCount(deleted)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getGreenhousesByOwner(GreenhousesByOwnerRequest request,
                                      StreamObserver<GreenhousesByOwnerResponse> responseObserver) {
        List<Long> ids = greenhouseService.getIdsByOwnerId(request.getOwnerId());
        responseObserver.onNext(GreenhousesByOwnerResponse.newBuilder()
                .addAllGreenhouseIds(ids)
                .build());
        responseObserver.onCompleted();
    }
}