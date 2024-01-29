# Neo4j Integration with Spring Framework - API Documentation

This documentation provides an overview of the API for learning Neo4j integration with the Spring framework. It adheres to the Swagger 3.0 (Open API) standard.

## Overview

This document covers the integration of Neo4j with the Spring framework, utilizing Swagger 3.0 (Open API) standards. For more details, please refer to the [GitHub repository](https://github.com/springdoc/springdoc-openapi?tab=readme-ov-file).

## Information

- **Title**: Swagger3.0 (Open API) Neo4j Integration with Spring Framework Learning Documentation
- **Description**: Learning documentation for Neo4j integration with the Spring framework
- **Contact Person**: sunwei (Email: lionsaid@aliyun.com)
- **License**: Apache 2.0 [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html)
- **Version**: 0.0.1

## Servers

- Local Server: [http://localhost:8080](http://localhost:8080)

## Tags

1. **Neo4jNode Api**: API for nodes [Neo4j Docs](https://neo4j.com/docs/cypher-manual/current/queries/concepts/)
2. **Neo4j doctor Api**: API for doctors [Neo4j Docs](https://neo4j.com/docs/cypher-manual/current/queries/concepts/)
3. **Neo4jRelation Api**: API for node relationships [Neo4j Docs](https://neo4j.com/docs/cypher-manual/current/queries/concepts/)
4. **Database Synchronization to Neo4j Api**: Synchronize database data to Neo4j [Neo4j Docs](https://neo4j.com/docs/cypher-manual/current/queries/concepts/)

## Paths

### /neo4j/relation

- **PUT**: Update Edge
    - Request Body:
      ```json
      {
        "source": {...},
        "target": {...},
        "property": {...},
        "where": {...},
        "direction": "left",
        "name": "string",
        "aliasName": "string",
        "id": "string"
      }
      ```
    - Response:
      ```plaintext
      200 OK
      ```

- **POST**: Add Edge
    - Request Body:
      ```json
      {
        "source": {...},
        "target": {...},
        "property": {...},
        "where": {...},
        "direction": "left",
        "name": "string",
        "aliasName": "string",
        "id": "string"
      }
      ```
    - Response:
      ```plaintext
      200 OK
      ```

- **DELETE**: Delete Edge
    - Request Body:
      ```json
      {
        "source": {...},
        "target": {...},
        "property": {...},
        "where": {...},
        "direction": "left",
        "name": "string",
        "aliasName": "string",
        "id": "string"
      }
      ```
    - Response:
      ```plaintext
      200 OK
      ```

### /neo4j/node

- **PUT**: Update Label
    - Request Body:
      ```json
      {
        "aliasName": "string",
        "labelName": "string",
        "where": {...},
        "property": {...}
      }
      ```
    - Response:
      ```plaintext
      200 OK
      ```

- **POST**: Add Label
    - Request Body:
      ```json
      {
        "aliasName": "string",
        "labelName": "string",
        "where": {...},
        "property": {...}
      }
      ```
    - Response:
      ```plaintext
      200 OK
      ```

- **DELETE**: Delete Label
    - Request Body:
      ```json
      {
        "aliasName": "string",
        "labelName": "string",
        "where": {...},
        "property": {...}
      }
      ```
    - Response:
      ```plaintext
      200 OK
      ```

### /neo4j/node/exist

- **POST**: Check Label Existence
    - Request Body:
      ```json
      {
        "aliasName": "string",
        "labelName": "string",
        "where": {...},
        "property": {...}
      }
      ```
    - Response:
      ```plaintext
      200 OK
      ```

### /neo4j/doctor/viewDetail

- **GET**: View Doctor Details
    - Parameters:
        - doctorId (query, required)
        - page (query, required, schema: Pageable)
    - Response:
      ```plaintext
      200 OK
      ```

### /neo4j/doctor/page

- **GET**: Query Doctors
    - Parameters:
        - dto (query, required, schema: 医生查询DTO)
        - page (query, required, schema: Pageable)
    - Response:
      ```plaintext
      200 OK
      ```

### /neo4j/doctor/chart

- **GET**: Chart Doctors
    - Parameters:
        - dto (query, required, schema: 医生查询DTO)
        - page (query, required, schema: Pageable)
    - Response:
      ```plaintext
      200 OK
      ```

### /database/databaseSynchronizationDataToNeo4j

- **GET**: Synchronize Database Data to Neo4j
    - Parameters:
        - type (query, required, example: node,relationship)
    - Response:
      ```plaintext
      200 OK
      ```

## Components

### Schemas

- **JSONObject**:
    - Type: object
    - Properties:
        - empty (boolean)
    - Additional Properties:
        - object (example: {"id":"121212","name":"apple"})
    - Example:
      ```json
      {
        "id": "121212",
        "name": "apple"
      }
      ```

- **RelationEntity**:
    - Title: 边的创建
    - Type: object
    - Properties:
        - source: 对象
        - target: 对象
        - property: JSONObject
        - where: JSONObject
        - direction: 边的方向 (enum: left, right)
        - name: 边的名称
        - aliasName: 边的别名
        - id: 关系id
    - Example:
      ```json
      {
        "source": {...},
        "target": {...},
        "property": {...},
        "where": {...},
        "direction": "left",
        "name": "string",
        "aliasName": "string",
        "id": "string"
      }
      ```

- **节点**:
    - Title: 目标
    - Type: object
    - Properties:
        - aliasName: 别名
        - labelName: label 名称
        - where: JSONObject
        - property: JSONObject
    - Example:
