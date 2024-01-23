package com.lionsaid.data.jpa.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "neo4j_relationship")
public class Neo4jRelationship {
    @Id
    private String id;
    @Schema(title = "来源")
    private String source;
    @Schema(title = "目标")
    private String target;
    @Schema(title = "边的属性")
    private String property;
    @Schema(title = "边的方向")
    private String direction;
    @Schema(title = "边的名称")
    private String name;
    @Schema(title = "边的别名")
    private String aliasName;
}
