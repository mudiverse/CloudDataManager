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

    @GetMapping("/{id}")
    public ResponseEntity<DataEntry> getById(@RequestHeader("X-API-KEY") String apiKey,
                                             @RequestParam String collectionName,
                                             @PathVariable String id) {
        return dataService.getById(apiKey, collectionName, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DataEntry> update(@RequestHeader("X-API-KEY") String apiKey,
                                            @RequestBody InsertRequest request,
                                            @PathVariable String id) {
        DataEntry updated = dataService.updateData(apiKey, request.collectionName(), id, request.data());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("X-API-KEY") String apiKey,
                                       @RequestParam String collectionName,
                                       @PathVariable String id) {
        dataService.deleteData(apiKey, collectionName, id);
        return ResponseEntity.noContent().build();
    }

    public record InsertRequest(@NotBlank String collectionName, Map<String, Object> data) {}
}


