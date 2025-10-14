CloudDBService
================

A lightweight cloud-backed backend service where each user registers to obtain an API key, defines custom MongoDB-backed collections (schemas), and inserts/fetches JSON data. Built with Spring Boot 3, Java 17, and MongoDB.

Environment
-----------
- `MONGODB_URI`: MongoDB connection string (e.g., `mongodb+srv://user:pass@cluster/db`)
- `MONGODB_DATABASE` (optional): Database name, default `clouddb`
- Runs on port `8080` (configurable in `application.properties`)

Build & Run
-----------
```bash
mvn clean package
java -jar target/CloudDBService-0.0.1-SNAPSHOT.jar
```

Headers
-------
- `X-API-KEY`: required for schema and data endpoints

Endpoints
---------
- POST `/api/user/register`
  - Body: `{ "name": "John Doe", "email": "john@example.com" }`
  - Response: `{ "name": "John Doe", "email": "john@example.com", "apiKey": "..." }`

- POST `/api/schema/create`
  - Headers: `X-API-KEY: <apiKey>`
  - Body: `{ "collectionName": "orders", "schemaDefinition": { "orderId": "string", "amount": "number" } }`
  - Response: the created schema document

- POST `/api/data/insert`
  - Headers: `X-API-KEY: <apiKey>`
  - Body: `{ "collectionName": "orders", "data": { "orderId": "o-1", "amount": 42.5 } }`
  - Response: the stored `DataEntry`

- GET `/api/data/fetch/{collectionName}`
  - Headers: `X-API-KEY: <apiKey>`
  - Response: `DataEntry[]` for the user and collection

Design Notes
------------
- Package structure under `com.cloudapi`: `controller`, `service`, `model`, `repository`, `config`, `exception`.
- `MongoTemplate` used to create/query dynamic per-user collections named `<apiKey>__<collectionName>`.
- Basic schema validation checks value types against provided schema definition strings.

AWS EC2 Deployment
------------------
1. Set environment variables on the instance: `MONGODB_URI`, optionally `MONGODB_DATABASE`.
2. Run the JAR with `java -jar` (or use a systemd service).
3. Ensure security group allows inbound TCP `8080` from your client.


