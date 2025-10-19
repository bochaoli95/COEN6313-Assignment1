package com.example.client;

import com.example.grpc.PrizeServiceGrpc;
import com.example.grpc.PrizeServiceProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Service
public class GrpcClientService {

    @Value("${grpc.server.host}")
    private String serverHost;

    @Value("${grpc.server.port}")
    private int serverPort;

    private ManagedChannel channel;
    private PrizeServiceGrpc.PrizeServiceBlockingStub blockingStub;

    public void initializeChannel() {
        channel = ManagedChannelBuilder.forAddress(serverHost, serverPort)
                .usePlaintext()
                .build();
        blockingStub = PrizeServiceGrpc.newBlockingStub(channel);
    }

    public PrizeServiceProto.CategoryYearRangeResponse searchByCategoryAndYearRange(
            String category, int startYear, int endYear) {
        
        if (blockingStub == null) {
            initializeChannel();
        }

        PrizeServiceProto.CategoryYearRangeRequest request = 
                PrizeServiceProto.CategoryYearRangeRequest.newBuilder()
                        .setCategory(category)
                        .setStartYear(startYear)
                        .setEndYear(endYear)
                        .build();

        return blockingStub.searchByCategoryAndYearRange(request);
    }

    public PrizeServiceProto.MotivationKeywordResponse searchByMotivationKeyword(String keyword) {
        if (blockingStub == null) {
            initializeChannel();
        }

        PrizeServiceProto.MotivationKeywordRequest request = 
                PrizeServiceProto.MotivationKeywordRequest.newBuilder()
                        .setKeyword(keyword)
                        .build();

        return blockingStub.searchByMotivationKeyword(request);
    }

    public PrizeServiceProto.LaureateNameResponse searchByLaureateName(
            String firstName, String lastName) {
        
        if (blockingStub == null) {
            initializeChannel();
        }

        PrizeServiceProto.LaureateNameRequest request = 
                PrizeServiceProto.LaureateNameRequest.newBuilder()
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .build();

        return blockingStub.searchByLaureateName(request);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
