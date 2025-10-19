package com.example.controller;

import com.example.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/load-prize-data")
    public ResponseEntity<Map<String, Object>> loadPrizeData() {
        Map<String, Object> response = new HashMap<>();
        try {
            redisService.loadPrizeDataToRedis();
            response.put("success", true);
            response.put("message", "Prize data loaded to Redis successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Failed to load prize data: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/prizes")
    public ResponseEntity<Map<String, Object>> searchPrizes(
            @RequestParam String category,
            @RequestParam int startYear,
            @RequestParam int endYear) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> results = redisService.searchPrizesByCategoryAndYearRange(category, startYear, endYear);
            response.put("success", true);
            response.put("category", category);
            response.put("startYear", startYear);
            response.put("endYear", endYear);
            response.put("count", results.size());
            response.put("results", results);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Search failed: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/motivation")
    public ResponseEntity<Map<String, Object>> searchByMotivation(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> results = redisService.searchPrizesByMotivationKeyword(keyword);
            response.put("success", true);
            response.put("keyword", keyword);
            response.put("totalLaureates", results.size());
            response.put("results", results);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Search failed: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/laureate")
    public ResponseEntity<Map<String, Object>> searchLaureateByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> results = redisService.searchLaureateByName(firstName, lastName);
            response.put("success", true);
            response.put("firstName", firstName);
            response.put("lastName", lastName);
            response.put("count", results.size());
            response.put("results", results);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Search failed: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}

