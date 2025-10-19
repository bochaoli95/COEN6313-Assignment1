package com.example.grpc;

import com.example.service.RedisService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PrizeServiceImpl extends PrizeServiceGrpc.PrizeServiceImplBase {

    @Autowired
    private RedisService redisService;

    @Override
    public void searchByCategoryAndYearRange(
            PrizeServiceProto.CategoryYearRangeRequest request,
            StreamObserver<PrizeServiceProto.CategoryYearRangeResponse> responseObserver) {

        try {
            List<Map<String, Object>> results = redisService.searchPrizesByCategoryAndYearRange(
                    request.getCategory(),
                    request.getStartYear(),
                    request.getEndYear()
            );

            // Build response object
            PrizeServiceProto.CategoryYearRangeResponse.Builder responseBuilder =
                    PrizeServiceProto.CategoryYearRangeResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("OK")
                            .setCount(results.size());

            for (Map<String, Object> item : results) {
                PrizeServiceProto.PrizeData prize = PrizeServiceProto.PrizeData.newBuilder()
                        .setKey((String) item.getOrDefault("key", ""))
                        .setYear(getIntValue(item.get("year")))
                        .setCategory((String) item.getOrDefault("category", ""))
                        .setLaureatesJson((String) item.getOrDefault("laureates_json", ""))
                        .build();
                responseBuilder.addPrizes(prize);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void searchByMotivationKeyword(
            PrizeServiceProto.MotivationKeywordRequest request,
            StreamObserver<PrizeServiceProto.MotivationKeywordResponse> responseObserver) {

        try {
            List<Map<String, Object>> results =
                    redisService.searchPrizesByMotivationKeyword(request.getKeyword());

            PrizeServiceProto.MotivationKeywordResponse.Builder responseBuilder =
                    PrizeServiceProto.MotivationKeywordResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("OK")
                            .setTotalLaureates(results.size());

            for (Map<String, Object> item : results) {
                PrizeServiceProto.PrizeData prize = PrizeServiceProto.PrizeData.newBuilder()
                        .setKey((String) item.getOrDefault("key", ""))
                        .setYear(getIntValue(item.get("year")))
                        .setCategory((String) item.getOrDefault("category", ""))
                        .setLaureatesJson((String) item.getOrDefault("laureates_json", ""))
                        .build();
                responseBuilder.addPrizes(prize);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void searchByLaureateName(
            PrizeServiceProto.LaureateNameRequest request,
            StreamObserver<PrizeServiceProto.LaureateNameResponse> responseObserver) {

        try {
            List<Map<String, Object>> results = redisService.searchLaureateByName(
                    request.getFirstName(),
                    request.getLastName()
            );

            PrizeServiceProto.LaureateNameResponse.Builder responseBuilder =
                    PrizeServiceProto.LaureateNameResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("OK")
                            .setCount(results.size());

            for (Map<String, Object> item : results) {
                PrizeServiceProto.LaureateData laureate =
                        PrizeServiceProto.LaureateData.newBuilder()
                                .setKey((String) item.getOrDefault("key", ""))
                                .setYear(getIntValue(item.get("year")))
                                .setCategory((String) item.getOrDefault("category", ""))
                                .setMotivation((String) item.getOrDefault("motivation", ""))
                                .setFirstName((String) item.getOrDefault("first_name", ""))
                                .setLastName((String) item.getOrDefault("last_name", ""))
                                .build();
                responseBuilder.addLaureates(laureate);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    private int getIntValue(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
