package com.lionsaid.data.neo4j.entity;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "节点")
public class NodeEntity {
    @Schema(title = "别名")
    private String aliasName;
    @Schema(title = "label 名称")
    private String labelName;
    @Schema(example = "{\"id\":\"121212\",\"name\":\"apple\"} ")
    private JSONObject where;
    @Schema(example = "{\"id\":\"121212\",\"name\":\"apple\"}")
    private JSONObject property;
}
