package com.cloudapi.service;

import com.cloudapi.model.Schema;
import com.cloudapi.repository.SchemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.Instant;

@Service
public class SchemaService {
    private final SchemaRepository schemaRepository;
    private final UserService userService;

    public SchemaService(SchemaRepository schemaRepository, UserService userService) {
        this.schemaRepository = schemaRepository;
        this.userService = userService;
    }

    public Schema createSchema(String apiKey, String collectionName, Map<String, String> schemaDefinition,
                               String displayName, String description) {
        validateApiKey(apiKey);
        Optional<Schema> existing = schemaRepository.findByUserApiKeyAndCollectionName(apiKey, collectionName);
        if (existing.isPresent()) {
            return existing.get();
        }
        Schema schema = Schema.builder()
                .userApiKey(apiKey)
                .collectionName(collectionName)
                .displayName(displayName != null && !displayName.isBlank() ? displayName : collectionName)
                .description(description)
                .createdAt(Instant.now())
                .schemaDefinition(schemaDefinition)
                .build();
        return schemaRepository.save(schema);
    }

    public Optional<Schema> getSchema(String apiKey, String collectionName) {
        validateApiKey(apiKey);
        return schemaRepository.findByUserApiKeyAndCollectionName(apiKey, collectionName);
    }

    public List<Schema> listSchemas(String apiKey) {
        validateApiKey(apiKey);
        return schemaRepository.findByUserApiKey(apiKey);
    }

    private void validateApiKey(String apiKey) {
        if (userService.getByApiKey(apiKey).isEmpty()) {
            throw new IllegalArgumentException("Invalid API key");
        }
    }
}


