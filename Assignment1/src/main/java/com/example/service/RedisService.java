package com.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private UnifiedJedis jedis;

    public void set(String key, Object value) {
        jedis.set(key, value.toString());
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        long seconds = unit.toSeconds(timeout);
        jedis.setex(key, (int) seconds, value.toString());
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public Long delete(String key) {
        return jedis.del(key);
    }

    public Boolean hasKey(String key) {
        return jedis.exists(key);
    }

    public Long expire(String key, long timeout, TimeUnit unit) {
        long seconds = unit.toSeconds(timeout);
        return jedis.expire(key, (int) seconds);
    }


    public void loadPrizeDataToRedis() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("prize.json");
        
        try (InputStream inputStream = resource.getInputStream()) {
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode prizesNode = rootNode.get("prizes");
            
            if (prizesNode.isArray()) {
                int prizeCounter = 1;
                for (JsonNode prizeNode : prizesNode) {
                    String key = "prizes:" + prizeCounter++;
                    
                    // Ensure year is stored as a numeric value
                    ObjectNode normalized = prizeNode.deepCopy();
                    if (normalized.has("year")) {
                        normalized.put("year", normalized.get("year").asInt());
                    }
                    
                    // Store as JSON in Redis
                    jedis.jsonSet(key, redis.clients.jedis.json.Path2.ROOT_PATH, normalized.toString());
                }
            }
        }
    }

    public List<Map<String, Object>> searchPrizesByCategoryAndYearRange(String category, int startYear, int endYear) {
        String queryString = String.format("(@category:{%s}) @year:[%d %d]", category, startYear, endYear);
        Query query = new Query(queryString)
                .returnFields("$.year", "$.category", "$.laureates");

        SearchResult result = jedis.ftSearch("prizeIdx", query);
        
        List<Map<String, Object>> results = new ArrayList<>();
        for (redis.clients.jedis.search.Document doc : result.getDocuments()) {
            Map<String, Object> prizeData = new HashMap<>();
            prizeData.put("key", doc.getId());
            prizeData.put("year", doc.getString("$.year"));
            prizeData.put("category", doc.getString("$.category"));
            prizeData.put("laureates", doc.getString("$.laureates"));
            results.add(prizeData);
        }
        
        return results;
    }

    public List<Map<String, Object>> searchPrizesByMotivationKeyword(String keyword) {
        String queryString = String.format("@motivation:%s", keyword);
        Query query = new Query(queryString)
                .returnFields("$.year", "$.category", "$.laureates");        
        SearchResult result = jedis.ftSearch("prizeIdx", query);
        
        List<Map<String, Object>> results = new ArrayList<>();
        for (redis.clients.jedis.search.Document doc : result.getDocuments()) {
            Map<String, Object> prizeData = new HashMap<>();
            prizeData.put("key", doc.getId());
            prizeData.put("year", doc.getString("$.year"));
            prizeData.put("category", doc.getString("$.category"));
            prizeData.put("laureates", doc.getString("$.laureates"));
            results.add(prizeData);
        }
        
        return results;
    }

    public List<Map<String, Object>> searchLaureateByName(String firstName, String lastName) {
        String queryString = String.format("@firstname:(%s) @surname:(%s)", firstName, lastName);
        Query query = new Query(queryString)
                .returnFields("$.year", "$.category", "$.laureates[*].motivation");
        
        SearchResult result = jedis.ftSearch("prizeIdx", query);
        
        List<Map<String, Object>> results = new ArrayList<>();
        for (redis.clients.jedis.search.Document doc : result.getDocuments()) {
            Map<String, Object> laureateData = new HashMap<>();
            laureateData.put("key", doc.getId());
            laureateData.put("year", doc.getString("$.year"));
            laureateData.put("category", doc.getString("$.category"));
            laureateData.put("motivation", doc.getString("$.laureates[*].motivation"));
            results.add(laureateData);
        }
        
        return results;
    }

}