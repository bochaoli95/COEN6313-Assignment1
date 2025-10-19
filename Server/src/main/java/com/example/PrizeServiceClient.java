package com.example;

import com.example.grpc.PrizeServiceGrpc;
import com.example.grpc.PrizeServiceProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PrizeServiceClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        PrizeServiceGrpc.PrizeServiceBlockingStub stub =
                PrizeServiceGrpc.newBlockingStub(channel);

        PrizeServiceProto.CategoryYearRangeRequest request =
                PrizeServiceProto.CategoryYearRangeRequest.newBuilder()
                        .setCategory("physics")
                        .setStartYear(2000)
                        .setEndYear(2020)
                        .build();

        PrizeServiceProto.CategoryYearRangeResponse response =
                stub.searchByCategoryAndYearRange(request);

        System.out.println("RPC Result " + response.getCount() + "records");
        channel.shutdown();
    }
}