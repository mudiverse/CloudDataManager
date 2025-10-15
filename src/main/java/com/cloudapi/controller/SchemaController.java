package com.cloudapi.controller;

import com.cloudapi.model.Schema;
import com.cloudapi.service.SchemaService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

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
        Schema schema = schemaService.createSchema(
                apiKey,
                request.collectionName(),
                request.schemaDefinition(),
                request.displayName(),
                request.description()
        );
        return ResponseEntity.ok(schema);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Schema>> list(@RequestHeader("X-API-KEY") String apiKey) {
        return ResponseEntity.ok(schemaService.listSchemas(apiKey));
    }

    @GetMapping("/{collectionName}")
    public ResponseEntity<Schema> get(@RequestHeader("X-API-KEY") String apiKey,
                                      @PathVariable String collectionName) {
        return schemaService.getSchema(apiKey, collectionName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public record CreateSchemaRequest(@NotBlank String collectionName,
                                      Map<String, String> schemaDefinition,
                                      String displayName,
                                      String description) {}
}


