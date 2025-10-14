package com.cloudapi.service;

import com.cloudapi.model.DataEntry;
import com.cloudapi.model.Schema;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DataService {

    private final MongoTemplate mongoTemplate;
    private final SchemaService schemaService;
    private final UserService userService;

    public DataService(MongoTemplate mongoTemplate, SchemaService schemaService, UserService userService) {
        this.mongoTemplate = mongoTemplate;
        this.schemaService = schemaService;
        this.userService = userService;
    }

    public DataEntry insertData(String apiKey, String collectionName, Map<String, Object> data) {
        validateApiKey(apiKey);
        Optional<Schema> schemaOptional = schemaService.getSchema(apiKey, collectionName);
        if (schemaOptional.isEmpty()) {
            throw new IllegalArgumentException("Schema not found for collection: " + collectionName);
        }

        // Optional: basic field validation based on schema definition (type names only)
        Map<String, String> schemaDef = schemaOptional.get().getSchemaDefinition();
        if (schemaDef != null && !schemaDef.isEmpty()) {
            for (Map.Entry<String, String> field : schemaDef.entrySet()) {
                String fieldName = field.getKey();
                String expectedType = field.getValue();
                if (data.containsKey(fieldName)) {
                    Object value = data.get(fieldName);
                    if (!isTypeMatch(value, expectedType)) {
                        throw new IllegalArgumentException("Field '" + fieldName + "' expected type " + expectedType);
                    }
                }
            }
        }

        DataEntry entry = DataEntry.builder()
                .userApiKey(apiKey)
                .collectionName(collectionName)
                .data(data)
                .build();

        return mongoTemplate.save(entry, resolveCollectionName(apiKey, collectionName));
    }

    public List<DataEntry> fetchData(String apiKey, String collectionName) {
        validateApiKey(apiKey);
        String resolved = resolveCollectionName(apiKey, collectionName);
        Query query = new Query();
        query.addCriteria(Criteria.where("userApiKey").is(apiKey));
        return mongoTemplate.find(query, DataEntry.class, resolved);
    }

    private String resolveCollectionName(String apiKey, String collectionName) {
        return apiKey + "__" + collectionName;
    }

    private void validateApiKey(String apiKey) {
        if (userService.getByApiKey(apiKey).isEmpty()) {
            throw new IllegalArgumentException("Invalid API key");
        }
    }

    private boolean isTypeMatch(Object value, String expectedType) {
        if (value == null || expectedType == null) return true;
        return switch (expectedType.toLowerCase()) {
            case "string" -> value instanceof String;
            case "int", "integer" -> value instanceof Integer || value instanceof Long;
            case "double", "float", "number" -> value instanceof Number;
            case "boolean", "bool" -> value instanceof Boolean;
            case "object", "map" -> value instanceof Map;
            case "array", "list" -> value instanceof List;
            default -> true;
        };
    }
}


