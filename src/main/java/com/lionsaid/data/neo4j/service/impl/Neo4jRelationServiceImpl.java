package com.lionsaid.data.neo4j.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.lionsaid.data.neo4j.entity.NodeEntity;
import com.lionsaid.data.neo4j.entity.RelationEntity;
import com.lionsaid.data.neo4j.enums.Direction;
import com.lionsaid.data.neo4j.service.Neo4jRelationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class Neo4jRelationServiceImpl implements Neo4jRelationService {

    private final Driver driver;


    @Override
    public void post(RelationEntity relationEntity) {
        JSONObject sourceProperty = relationEntity.getSource().getWhere();
        JSONObject targetProperty = relationEntity.getTarget().getWhere();
        if (sourceProperty.isEmpty() && targetProperty.isEmpty()) {
            return;
        }
        List<String> list = getStringList(relationEntity, sourceProperty, targetProperty);
        try (var session = driver.session()) {
            if (relationEntity.getDirection().equals(Direction.left)) {
                session.executeWriteWithoutResult(tx -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MATCH (" + relationEntity.getSource().getAliasName() + ":" + relationEntity.getSource().getLabelName() + "), (" + relationEntity.getTarget().getAliasName() + ":" + relationEntity.getTarget().getLabelName() + ")\n" +
                            " WHERE " + list.stream().collect(Collectors.joining(" and ")) + "\n" +
                            " CREATE (" + relationEntity.getSource().getAliasName() + ")-[" + relationEntity.getAliasName() + ":" + relationEntity.getName() + " " + relationEntity.getProperty().toJSONString().replaceAll("\"(\\w+)\":", "$1:") + "] -> (" + relationEntity.getTarget().getAliasName() + ")\n");
                    log.debug(" Query [{}]", stringBuilder);
                    var query = new Query(stringBuilder.toString());
                    tx.run(query);
                });
            } else {
                session.executeWriteWithoutResult(tx -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MATCH (" + relationEntity.getSource().getAliasName() + ":" + relationEntity.getSource().getLabelName() + "), (" + relationEntity.getTarget().getAliasName() + ":" + relationEntity.getTarget().getLabelName() + ")\n" +
                            " WHERE " + list.stream().collect(Collectors.joining(" and ")) + "\n" +
                            " CREATE (" + relationEntity.getSource().getAliasName() + ")<-[" + relationEntity.getAliasName() + ":" + relationEntity.getName() + " " + relationEntity.getProperty().toJSONString().replaceAll("\"(\\w+)\":", "$1:") + "] - (" + relationEntity.getTarget().getAliasName() + ")\n");
                    log.debug(" Query [{}]", stringBuilder);
                    var query = new Query(stringBuilder.toString());
                    tx.run(query);
                });
            }
        }
    }

    private static List<String> getStringList(RelationEntity relationEntity, JSONObject sourceProperty, JSONObject targetProperty) {
        List<String> list = Lists.newArrayList();
        sourceProperty.forEach((k, v) -> {
            {
                if (v instanceof String) {
                    // 如果是String类型，用双引号括起来
                    list.add(relationEntity.getSource().getAliasName() + "." + k + " = \"" + v + "\"");
                } else {
                    // 其他类型直接拼接
                    list.add(relationEntity.getSource().getAliasName() + "." + k + " = " + v);
                }
            }
        });
        targetProperty.forEach((k, v) -> {
            {
                if (v instanceof String) {
                    // 如果是String类型，用双引号括起来
                    list.add(relationEntity.getTarget().getAliasName() + "." + k + " = \"" + v + "\"");
                } else {
                    // 其他类型直接拼接
                    list.add(relationEntity.getTarget().getAliasName() + "." + k + " = " + v);
                }
            }
        });
        return list;
    }

    @Override
    public void put(RelationEntity relationEntity) {
        JSONObject sourceProperty = relationEntity.getSource().getWhere();
        JSONObject targetProperty = relationEntity.getTarget().getWhere();
        if (sourceProperty.isEmpty() && targetProperty.isEmpty()) {
            return;
        }
        List<String> list = getStringList(relationEntity, sourceProperty, targetProperty);
        JSONObject property = relationEntity.getProperty();
        if (property.isEmpty()) {
            return;
        }
        List<String> list1 = Lists.newArrayList();
        property.forEach((k, v) -> {
            {
                if (v instanceof String) {
                    // 如果是String类型，用双引号括起来
                    list1.add(relationEntity.getAliasName() + "." + k + " = \"" + v + "\"");
                } else if (v instanceof JSONObject) {
                    // JSONObject，用双引号括起来
                    list1.add(relationEntity.getAliasName() + "." + k + " = \"" + ((JSONObject) v).toJSONString() + "\"");
                } else if (v instanceof JSONArray) {
                    // 如果是JSONArray类型，用双引号括起来
                    list1.add(relationEntity.getAliasName() + "." + k + " = \"" + ((JSONArray) v).toJSONString() + "\"");
                } else {
                    // 其他类型直接拼接
                    list1.add(relationEntity.getAliasName() + "." + k + " = " + v);
                }

            }
        });
        try (var session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MATCH (" + relationEntity.getSource().getAliasName() + ":" + relationEntity.getSource().getLabelName() + ")-[" + relationEntity.getAliasName() + ":" + relationEntity.getName() + "]-(" + relationEntity.getTarget().getAliasName() + ":" + relationEntity.getTarget().getLabelName() + ")\n" +
                        " WHERE " + list.stream().collect(Collectors.joining(" AND ")) +
                        " SET " + list1.stream().collect(Collectors.joining(",")));
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                tx.run(query);
            });
        }
    }

    @Override
    public void delete(RelationEntity relationEntity) {
        JSONObject sourceProperty = relationEntity.getSource().getWhere();
        JSONObject targetProperty = relationEntity.getTarget().getWhere();
        if (sourceProperty.isEmpty() && targetProperty.isEmpty()) {
            return;
        }
        List<String> list = getStringList(relationEntity, sourceProperty, targetProperty);
        try (var session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MATCH (" + relationEntity.getSource().getAliasName() + ":" + relationEntity.getSource().getLabelName() + ")-[" + relationEntity.getAliasName() + ":" + relationEntity.getName() + "]-(" + relationEntity.getTarget().getAliasName() + ":" + relationEntity.getTarget().getLabelName() + ")\n" +
                        " WHERE " + list.stream().collect(Collectors.joining(" AND ")) +
                        " DELETE " + relationEntity.getAliasName() + "");
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                tx.run(query);
            });
        }
    }

    @Override
    public Boolean exist(RelationEntity relationEntity) {
        ArrayList<@Nullable String> where = Lists.newArrayList();
        NodeEntity source = relationEntity.getSource();
        NodeEntity target = relationEntity.getTarget();
        source.getWhere().forEach((s, o) -> where.add(source.getAliasName() + "." + s + "= \"" + o + "\""));
        target.getWhere().forEach((s, o) -> where.add(target.getAliasName() + "." + s + "= \"" + o + "\""));

        JSONObject property = new JSONObject();
        property.put("id", relationEntity.getId());
        try (var session = driver.session()) {
            return session.readTransaction(tx -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(
                        "MATCH (" + source.getAliasName() + ":" + source.getLabelName() + ")-[" + relationEntity.getAliasName() + ":" + relationEntity.getName() + " " + property.toJSONString().replaceAll("\"(\\w+)\":", "$1:") + "]->(" + target.getAliasName() + ":" + target.getLabelName() + ")\n" +
                                "where " + where.stream().collect(Collectors.joining(" and ")) +
                                "RETURN COUNT(" + relationEntity.getAliasName() + ") > 0 AS exists");
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                var result = tx.run(query);
                return result.single().get("exists").asBoolean();
            });
        }
    }
}
