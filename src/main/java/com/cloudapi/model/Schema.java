package com.cloudapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Map<String, String> schemaDefinition;
}


