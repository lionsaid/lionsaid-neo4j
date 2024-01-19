package com.lionsaid.data.jpa.controller;

import com.lionsaid.data.jpa.service.Neo4jLabelService;
import com.lionsaid.data.jpa.service.Neo4jRelationshipService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("database")
@RestController
@AllArgsConstructor
@Tag(
        name = "同步数据库数据到neo4j Api",
        description = "同步数据库数据到neo4j",
        externalDocs = @ExternalDocumentation(
                description = "neo4j docs",
                url = "https://neo4j.com/docs/cypher-manual/current/queries/concepts/"))
public class DatabaseController {
    private final Neo4jLabelService neo4jLabelService;
    private final Neo4jRelationshipService neo4jRelationshipService;

    /**
     * @return
     */
    @GetMapping("databaseSynchronizationDataToNeo4j")
    @Operation(summary = "同步数据库数据到neo4j", parameters = {@Parameter(name = "type", example = "node,relationship")})
    public ResponseEntity<String> databaseSynchronizationDataToNeo4j(@RequestParam String type) {
        switch (type.toLowerCase()) {
            case "node" -> neo4jLabelService.databaseSynchronizationDataToNeo4j();
            case "relationship" -> neo4jRelationshipService.databaseSynchronizationDataToNeo4j();
            default -> log.error("不支持的类型");
        }
        return ResponseEntity.ok("");
    }


}
