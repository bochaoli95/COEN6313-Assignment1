package com.example.client;

import com.example.grpc.PrizeServiceProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/prize")
public class PrizeController {

    @Autowired
    private GrpcClientService grpcClientService;

    @GetMapping("/search/category-year")
    public ResponseEntity<Map<String, Object>> searchByCategoryAndYearRange(
            @RequestParam String category,
            @RequestParam int startYear,
            @RequestParam int endYear) {
        
        try {
            PrizeServiceProto.CategoryYearRangeResponse response = 
                    grpcClientService.searchByCategoryAndYearRange(category, startYear, endYear);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.getSuccess());
            result.put("message", response.getMessage());
            result.put("count", response.getCount());
            result.put("prizes", response.getPrizesList());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/search/motivation")
    public ResponseEntity<Map<String, Object>> searchByMotivationKeyword(
            @RequestParam String keyword) {
        
        try {
            PrizeServiceProto.MotivationKeywordResponse response = 
                    grpcClientService.searchByMotivationKeyword(keyword);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.getSuccess());
            result.put("message", response.getMessage());
            result.put("totalLaureates", response.getTotalLaureates());
            result.put("prizes", response.getPrizesList());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/search/laureate")
    public ResponseEntity<Map<String, Object>> searchByLaureateName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        
        try {
            PrizeServiceProto.LaureateNameResponse response = 
                    grpcClientService.searchByLaureateName(firstName, lastName);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.getSuccess());
            result.put("message", response.getMessage());
            result.put("count", response.getCount());
            result.put("laureates", response.getLaureatesList());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
