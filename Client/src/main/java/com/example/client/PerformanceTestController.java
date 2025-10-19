package com.example.client;

import com.example.grpc.PrizeServiceProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/test")
public class PerformanceTestController {

    @Autowired
    private GrpcClientService grpcClientService;

    @GetMapping("/category-year")
    public Map<String, Object> testCategoryYear(
            @RequestParam(defaultValue = "physics") String category,
            @RequestParam(defaultValue = "2000") int startYear,
            @RequestParam(defaultValue = "2020") int endYear) {

        List<Long> delays = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            grpcClientService.searchByCategoryAndYearRange(category, startYear, endYear);
            long end = System.currentTimeMillis();
            delays.add(end - start);
        }

        String filePath = writeToFile("category_year.txt", delays);
        Map<String, Object> result = new HashMap<>();
        result.put("file", filePath);
        result.put("average", delays.stream().mapToLong(Long::longValue).average().orElse(0));
        result.put("min", Collections.min(delays));
        result.put("max", Collections.max(delays));
        result.put("count", delays.size());
        return result;
    }

    @GetMapping("/motivation")
    public Map<String, Object> testMotivation(
            @RequestParam(defaultValue = "quantum") String keyword) {

        List<Long> delays = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            grpcClientService.searchByMotivationKeyword(keyword);
            long end = System.currentTimeMillis();
            delays.add(end - start);
        }

        String filePath = writeToFile("motivation.txt", delays);
        Map<String, Object> result = new HashMap<>();
        result.put("file", filePath);
        result.put("average", delays.stream().mapToLong(Long::longValue).average().orElse(0));
        result.put("min", Collections.min(delays));
        result.put("max", Collections.max(delays));
        result.put("count", delays.size());
        return result;
    }

    @GetMapping("/laureate")
    public Map<String, Object> testLaureate(
            @RequestParam(defaultValue = "Albert") String firstName,
            @RequestParam(defaultValue = "Einstein") String lastName) {

        List<Long> delays = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            grpcClientService.searchByLaureateName(firstName, lastName);
            long end = System.currentTimeMillis();
            delays.add(end - start);
        }

        String filePath = writeToFile("laureate.txt", delays);
        Map<String, Object> result = new HashMap<>();
        result.put("file", filePath);
        result.put("average", delays.stream().mapToLong(Long::longValue).average().orElse(0));
        result.put("min", Collections.min(delays));
        result.put("max", Collections.max(delays));
        result.put("count", delays.size());
        return result;
    }

    private String writeToFile(String filename, List<Long> data) {
        String dirPath = System.getProperty("user.dir") + File.separator + "results";
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        String filePath = dirPath + File.separator + filename;
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Long value : data) writer.write(value + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
