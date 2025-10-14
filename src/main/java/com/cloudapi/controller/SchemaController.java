package com.cloudapi.controller;

import com.cloudapi.model.Schema;
import com.cloudapi.service.SchemaService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/schema")
public class SchemaController {

    private final SchemaService schemaService;

    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @PostMapping("/create")
    public ResponseEntity<Schema> create(@RequestHeader("X-API-KEY") String apiKey,
                                         @RequestBody CreateSchemaRequest request) {
        Schema schema = schemaService.createSchema(apiKey, request.collectionName(), request.schemaDefinition());
        return ResponseEntity.ok(schema);
    }

    public record CreateSchemaRequest(@NotBlank String collectionName, Map<String, String> schemaDefinition) {}
}


