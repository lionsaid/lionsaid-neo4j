package com.lionsaid.data.neo4j.controller;

import com.lionsaid.data.neo4j.entity.RelationEntity;
import com.lionsaid.data.neo4j.service.Neo4jRelationService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("neo4j/relation")
@RestController
@AllArgsConstructor
@Tag(
        name = "Neo4jRelation Api",
        description = "节点关系的api",
        externalDocs = @ExternalDocumentation(
                description = "neo4j docs",
                url = "https://neo4j.com/docs/cypher-manual/current/queries/concepts/"))
public class Neo4jRelationController {


    private final Neo4jRelationService neo4jRelationService;

    /**
     * @return
     */
    @PostMapping
    @Operation(summary = "新增边")
    public ResponseEntity<String> post(@RequestBody RelationEntity entity) {
        neo4jRelationService.post(entity);
        return ResponseEntity.ok("");
    }

    @PutMapping
    @Operation(summary = "更新边")
    public ResponseEntity<String> put(@RequestBody RelationEntity entity) {
        neo4jRelationService.put(entity);
        return ResponseEntity.ok("");
    }

    @DeleteMapping
    @Operation(summary = "删除边")
    public ResponseEntity<String> delete(@RequestBody RelationEntity entity) {
        neo4jRelationService.delete(entity);
        return ResponseEntity.ok("");
    }


}
