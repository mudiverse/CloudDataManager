package com.cloudapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "schemas")
public class Schema {
    @Id
    private String id;
    private String userApiKey;
    private String collectionName;
    private String displayName;
    private String description;
    private Instant createdAt;
    private Map<String, String> schemaDefinition;
}


