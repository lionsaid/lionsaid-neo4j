package com.lionsaid.data.neo4j.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lionsaia.data.neo4j.entity.dto.DoctorSearchDTO;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("neo4j/doctor")
@RestController
@AllArgsConstructor
@Tag(name = "Neo4j doctor Api", description = "doctor的api", externalDocs = @ExternalDocumentation(description = "neo4j docs", url = "https://neo4j.com/docs/cypher-manual/current/queries/concepts/"))
public class Neo4jDoctorInfoController {


    private final Driver driver;

    private static final Map<String, String> legendMap = new HashMap<>();
    private static final JSONObject itemStyle = new JSONObject();
    private static final JSONObject lineStyle = new JSONObject();

    static {
        legendMap.put("1", "师生");
        legendMap.put("2", "同事");
        legendMap.put("3", "论文合作");
        legendMap.put("4", "校友");
        legendMap.put("5", "会议");
        legendMap.put("6", "临床");

        itemStyle.put("borderColor", "#28c0c2");
        itemStyle.put("color", "#ffffff");
        itemStyle.put("shadowBlur", 10);
        itemStyle.put("borderWidth", 3);
        itemStyle.put("shadowColor", "rgba(0, 0, 0, 0.3)");
        lineStyle.put("curveness", 0.2);
        lineStyle.put("color", "#28c0c2");

    }

    @GetMapping("/page")
    @Operation(summary = "查询医生")
    public ResponseEntity page(DoctorSearchDTO dto, Pageable page) {
        try (var session = driver.session()) {
            JSONArray list1 = session.executeRead(tx -> {
                JSONArray list = new JSONArray();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MATCH (a:IdataDoctorInfo)-[r:DOCTOR_RIGHT_DOCTOR]->(b:IdataDoctorInfo)\n");
                if (StringUtils.isNotEmpty(dto.getWhere())) {
                    stringBuilder.append(" where " + dto.getWhere());
                }
                stringBuilder.append("WITH a.doctorId AS doctorId,a.doctorName AS doctorName, a.hospital AS hospital, a.department AS department, a.hospitalLevel AS hospitalLevel, a.doctorTitle AS doctorTitle, COUNT(r) AS relationshipCount, sum(r.doctorValue) AS doctorValue\n" + " RETURN doctorId,doctorName, hospital, department, hospitalLevel, doctorTitle, relationshipCount, doctorValue\n" + " ORDER BY " + page.getSort().stream().map(o -> {
                    return o.getProperty();
                }).toList().stream().collect(Collectors.joining(" , ")) + "\n" + " SKIP " + (page.getPageNumber() * page.getPageSize()) + " LIMIT " + page.getPageSize());
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                Result result = tx.run(query);
                result.list().forEach(o -> {
                    JSONObject jsonObject = new JSONObject();
                    o.keys().forEach(o1 -> jsonObject.put(o1, o.get(o1).asObject()));
                    list.add(jsonObject);
                });
                return list;
            });
            return ResponseEntity.ok(list1);
        }
    }

    @GetMapping("/viewDetail")
    @Operation(summary = "查询医生")
    public ResponseEntity viewDetail(@RequestParam String doctorId, Pageable page) {
        try (var session = driver.session()) {
            JSONArray list1 = session.executeRead(tx -> {
                JSONArray list = new JSONArray();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MATCH p=(a:IdataDoctorInfo)-[r:DOCTOR_RIGHT_DOCTOR]->(b:IdataDoctorInfo)\n" + "where a.doctorId=$doctorId\n" + "RETURN r.doctorValue  AS doctorValue,r.relationType AS relationType,b.doctorId AS doctorId,b.doctorName AS doctorName,b.hospital AS hospital,b.department AS department" + " ORDER BY " + page.getSort().stream().map(o -> {
                    return o.getProperty();
                }).toList().stream().collect(Collectors.joining(" , ")) + "\n" + " SKIP " + (page.getPageNumber() * page.getPageSize()) + " LIMIT " + page.getPageSize());
                Value parameters = Values.parameters("doctorId", doctorId);
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString(), parameters);
                Result result = tx.run(query);
                result.list().forEach(o -> {
                    JSONObject jsonObject = new JSONObject();
                    o.keys().forEach(o1 -> jsonObject.put(o1, o.get(o1).asObject()));
                    list.add(jsonObject);
                });
                return list;
            });
            return ResponseEntity.ok(list1);
        }
    }

    @GetMapping("/chart")
    @Operation(summary = "查询医生")
    public ResponseEntity chart(DoctorSearchDTO dto, Pageable page) {
        try (var session = driver.session()) {
            JSONObject list1 = session.executeRead(tx -> {
                JSONObject jsonObject = new JSONObject();
                HashMap<@Nullable String, @Nullable JSONObject> nodeArray = Maps.newHashMap();
                HashMap<@Nullable String, @Nullable JSONObject> linkArray = Maps.newHashMap();
                JSONArray list = new JSONArray();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MATCH (a:IdataDoctorInfo)-[r:DOCTOR_RIGHT_DOCTOR]->(b:IdataDoctorInfo)\n");
                if (StringUtils.isNotEmpty(dto.getWhere())) {
                    stringBuilder.append(" where " + dto.getWhere());
                }
                stringBuilder.append(
                        "WITH a, b, r, sum(r.doctorValue) AS doctorValue\n" +
                                "RETURN a, b, r, doctorValue   SKIP " + (page.getPageNumber() * page.getPageSize()) + " LIMIT " + page.getPageSize());
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                Result result = tx.run(query);
                List<Record> list12 = result.list();
                ArrayList<@Nullable Integer> a1 = Lists.newArrayList();
                list12.forEach(o -> a1.add(o.get("doctorValue").asInt()));
                Optional<Integer> max = a1.stream().filter(Objects::nonNull).max(Integer::compareTo);
                Optional<Integer> min = a1.stream().filter(Objects::nonNull).min(Integer::compareTo);
                jsonObject.put("min", min.orElse(0));
                jsonObject.put("max", max.orElse(100));
                jsonObject.put("chartType", "graph");

                list12.forEach(o -> {
                    JSONObject a = new JSONObject();
                    JSONObject b = new JSONObject();
                    a.putAll(o.get("a").asMap());
                    b.putAll(o.get("b").asMap());
                    getNode(nodeArray, max.orElse(100), o.get("doctorValue").asInt(), a);
                    getNode(nodeArray, max.orElse(100), o.get("doctorValue").asInt(), b);
                    getLink(linkArray, o.get("r").asMap(), a.getString("doctorId"), b.getString("doctorId"));
                });
                jsonObject.put("linkArray", linkArray.values());
                jsonObject.put("nodeArray", nodeArray.values());
                return jsonObject;
            });
            return ResponseEntity.ok(list1);
        }
    }


    private void getNode(HashMap<@Nullable String, @Nullable JSONObject> nodeArray, Integer max, Integer doctorValue, JSONObject jsonObject) {
        JSONObject node = new JSONObject();
        node.put("id", jsonObject.getString("doctorId"));
        node.put("name", jsonObject.getString("name"));
        node.put("department", jsonObject.getString("department"));
        node.put("doctorId", jsonObject.getString("doctorId"));
        node.put("symbolSize", max);
        node.put("value", doctorValue);
        node.put("x", null);
        node.put("y", null);
        node.put("security", true);
        node.put("itemStyle", itemStyle);
        nodeArray.put(jsonObject.getString("doctorId"), node);
    }

    private void getLink(HashMap<@Nullable String, @Nullable JSONObject> nodeArray, Map a, String source, String target) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(a);
        JSONObject link = new JSONObject();
        link.put("id", jsonObject.getString("id"));
        link.put("source", source);
        link.put("target", target);
        link.put("lineType", getLegend(jsonObject.getString("relationType")));
        link.put("value", jsonObject.getString("doctorValue"));
        link.put("lineStyle", lineStyle);
        nodeArray.put(jsonObject.getString("id"), link);
    }

    private String getLegend(String r) {
        for (Map.Entry<String, String> entry : legendMap.entrySet()) {
            r = r.replace(entry.getKey(), entry.getValue());
        }
        return r;
    }
}
