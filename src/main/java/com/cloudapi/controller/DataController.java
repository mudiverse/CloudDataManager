package com.cloudapi.controller;

import com.cloudapi.model.DataEntry;
import com.cloudapi.service.DataService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/insert")
    public ResponseEntity<DataEntry> insert(@RequestHeader("X-API-KEY") String apiKey,
                                            @RequestBody InsertRequest request) {
        DataEntry saved = dataService.insertData(apiKey, request.collectionName(), request.data());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/fetch/{collectionName}")
    public ResponseEntity<List<DataEntry>> fetch(@RequestHeader("X-API-KEY") String apiKey,
                                                 @PathVariable String collectionName) {
        List<DataEntry> result = dataService.fetchData(apiKey, collectionName);
        return ResponseEntity.ok(result);
    }

    public record InsertRequest(@NotBlank String collectionName, Map<String, Object> data) {}
}


