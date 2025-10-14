package com.cloudapi.repository;

import com.cloudapi.model.Schema;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SchemaRepository extends MongoRepository<Schema, String> {
    Optional<Schema> findByUserApiKeyAndCollectionName(String userApiKey, String collectionName);
    List<Schema> findByUserApiKey(String userApiKey);
}


