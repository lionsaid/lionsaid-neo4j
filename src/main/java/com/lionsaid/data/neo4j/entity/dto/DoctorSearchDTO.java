package com.lionsaia.data.neo4j.entity.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "医生查询DTO")
public class DoctorSearchDTO {
    @Schema(title = "区县")
    private String county;
    @Schema(title = "城市")
    private String city;
    @Schema(title = "省份")
    private String province;
    @Schema(example = "医生姓名")
    private String doctorName;
    @Schema(example = "医院")
    private String hospital;
    @Schema(example = "科室")
    private String department;
    @Schema(example = "搜索字段")
    private String search;
    @Schema(example = "搜索字段")
    private String academicKeywords;
    @Schema(example = "关系类型")
    private String relationType;

    @Hidden
    public String getWhere() {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(relationType)) {
            stringBuilder.append(" ( ");
            stringBuilder.append(Arrays.stream(relationType.split(" "))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .map(s -> "r.relationType =~ " + "'.*" + s + ".*'").collect(Collectors.joining(" OR ")));
            stringBuilder.append(" ) ");
        }
        if (StringUtils.isNotEmpty(county)) {
            stringBuilder.append(" a.county in [" + Arrays.stream(county.split(",")).distinct().map(s -> "'" + s + "'").collect(Collectors.joining(",")) + "] ");
        }
        if (StringUtils.isNotEmpty(city)) {
            stringBuilder.append(" a.city in [" + Arrays.stream(city.split(",")).distinct().map(s -> "'" + s + "'").collect(Collectors.joining(",")) + "] ");
        }
        if (StringUtils.isNotEmpty(province)) {
            stringBuilder.append(" a.province in [" + Arrays.stream(province.split(",")).distinct().map(s -> "'" + s + "'").collect(Collectors.joining(",")) + "] ");
        }
        if (StringUtils.isNotEmpty(hospital)) {
            stringBuilder.append(" a.hospital in [" + Arrays.stream(hospital.split(",")).distinct().map(s -> "'" + s + "'").collect(Collectors.joining(",")) + "] ");
        }
        if (StringUtils.isNotEmpty(department)) {
            stringBuilder.append(" a.department in [" + Arrays.stream(department.split(",")).distinct().map(s -> "'" + s + "'").collect(Collectors.joining(",")) + "] ");
        }
        if (StringUtils.isNotEmpty(search)) {
            stringBuilder.append(" ( ");
            stringBuilder.append(Arrays.stream(search.split(" "))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .map(s -> " a.searchField =~ " + "'.*" + s + ".*'").collect(Collectors.joining(" OR ")));
            stringBuilder.append(" ) ");
        }
        if (StringUtils.isNotEmpty(academicKeywords)) {
            stringBuilder.append(" ( ");
            stringBuilder.append(Arrays.stream(academicKeywords.split(" "))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .map(s -> " a.academicFocus =~ " + "'.*" + s + ".*'").collect(Collectors.joining(" OR ")));
            stringBuilder.append(" ) ");
        }
        return stringBuilder.toString();
    }


}
