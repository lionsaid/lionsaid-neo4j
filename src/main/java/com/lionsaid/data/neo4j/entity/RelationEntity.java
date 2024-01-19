package com.lionsaid.data.neo4j.entity;

import com.alibaba.fastjson2.JSONObject;
import com.lionsaid.data.neo4j.enums.Direction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "边的创建")
public class RelationEntity {
    @Schema(title = "来源")
    private NodeEntity source;
    @Schema(title = "目标")
    private NodeEntity target;
    @Schema(title = "边的属性")
    private JSONObject property;
    @Schema(title = "边的查询")
    private JSONObject where;
    @Schema(title = "边的方向")
    private Direction direction;
    @Schema(title = "边的名称")
    private String name;
    @Schema(title = "边的别名")
    private String aliasName;
    @Schema(title = "关系id")
    private String id;
}
