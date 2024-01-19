package com.lionsaid.data.neo4j.controller;

import com.lionsaid.data.neo4j.entity.NodeEntity;
import com.lionsaid.data.neo4j.service.Neo4jNodeService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("neo4j/node")
@RestController
@AllArgsConstructor
@Tag(
        name = "Neo4jNode Api",
        description = "节点的api",
        externalDocs = @ExternalDocumentation(
                description = "neo4j docs",
                url = "https://neo4j.com/docs/cypher-manual/current/queries/concepts/"))
public class Neo4jNodeController {


    private final Neo4jNodeService neo4jService;

    /**
     * @param node 创建Node
     * @return
     */
    @PostMapping
    @Operation(summary = "新增label")
    public ResponseEntity<String> post(@RequestBody NodeEntity node) {
        neo4jService.post(node);
        return ResponseEntity.ok("");
    }

    @PutMapping
    @Operation(summary = "更新label")
    public ResponseEntity<String> put(@RequestBody NodeEntity node) {
        neo4jService.put(node);
        return ResponseEntity.ok("");
    }

    @DeleteMapping
    @Operation(summary = "删除label")
    public ResponseEntity<String> delete(@RequestBody NodeEntity node) {
        neo4jService.delete(node);
        return ResponseEntity.ok("");
    }

    @PostMapping("/exist")
    @Operation(summary = "查询label是否存在")
    public ResponseEntity<Boolean> exist(@RequestBody NodeEntity node) {
        return ResponseEntity.ok(neo4jService.exist(node));
    }


}
