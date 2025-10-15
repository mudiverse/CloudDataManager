package com.cloudapi.controller;

import com.cloudapi.model.Schema;
import com.cloudapi.service.SchemaService;
import com.cloudapi.service.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/collections")
public class CollectionsController {

    private final SchemaService schemaService;
    private final DataService dataService;

    public CollectionsController(SchemaService schemaService, DataService dataService) {
        this.schemaService = schemaService;
        this.dataService = dataService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<CollectionInfo>> list(@RequestHeader("X-API-KEY") String apiKey) {
        List<CollectionInfo> result = schemaService.listSchemas(apiKey)
                .stream()
                .map(s -> new CollectionInfo(
                        s.getCollectionName(),
                        s.getDisplayName() != null ? s.getDisplayName() : s.getCollectionName(),
                        s.getDescription(),
                        dataService.countDocuments(apiKey, s.getCollectionName()),
                        s.getCreatedAt() != null ? s.getCreatedAt().toString() : null
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    public record CollectionInfo(String name, String displayName, String description, long documentCount, String createdAt) {}
}

