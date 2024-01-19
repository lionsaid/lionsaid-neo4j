package com.lionsaid.data.jpa.entity;

import com.lionsaid.data.neo4j.enums.Direction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Direction direction;
    @Schema(title = "边的名称")
    private String name;
    @Schema(title = "边的别名")
    private String aliasName;
}
