package com.cloudapi.controller;

import com.cloudapi.model.Schema;
import com.cloudapi.service.SchemaService;
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

    public CollectionsController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> list(@RequestHeader("X-API-KEY") String apiKey) {
        List<String> names = schemaService.listSchemas(apiKey)
                .stream()
                .map(Schema::getCollectionName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(names);
    }
}
