package com.lionsaid.data.neo4j.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.lionsaid.data.neo4j.entity.NodeEntity;
import com.lionsaid.data.neo4j.service.Neo4jNodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class Neo4jNodeServiceImpl implements Neo4jNodeService {

    private final Driver driver;


    @Override
    public void post(NodeEntity nodeEntity) {
        try (var session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CREATE (\n" +
                        "   " + nodeEntity.getAliasName() + ":" + nodeEntity.getLabelName() + "\n" +
                        "   " + nodeEntity.getProperty().toJSONString().replaceAll("\"(\\w+)\":", "$1:") + "\n" +
                        ")");
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                tx.run(query);
            });
        }
    }

    @Override
    public void put(NodeEntity nodeEntity) {
        JSONObject property = nodeEntity.getProperty();
        if (property.isEmpty()) {
            return;
        }
        List<String> list = Lists.newArrayList();
        property.forEach((k, v) -> {
            {
                if (v instanceof String) {
                    // 如果是String类型，用双引号括起来
                    list.add(nodeEntity.getAliasName() + "." + k + " = \"" + v + "\"");
                } else if (v instanceof JSONObject) {
                    // JSONObject，用双引号括起来
                    list.add(nodeEntity.getAliasName() + "." + k + " = \"" + ((JSONObject) v).toJSONString() + "\"");
                } else if (v instanceof JSONArray) {
                    // 如果是JSONArray类型，用双引号括起来
                    list.add(nodeEntity.getAliasName() + "." + k + " = \"" + ((JSONArray) v).toJSONString() + "\"");
                } else {
                    // 其他类型直接拼接
                    list.add(nodeEntity.getAliasName() + "." + k + " = " + v);
                }

            }
        });
        // 使用正则表达式替换属性名称
        try (var session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MATCH (" + nodeEntity.getAliasName() + ":" + nodeEntity.getLabelName() + " " + nodeEntity.getWhere().toJSONString().replaceAll("\"(\\w+)\":", "$1:") + ")\n" +
                        "SET " + list.stream().collect(Collectors.joining(",")));
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                tx.run(query);
            });
        }
    }

    @Override
    public Boolean exist(NodeEntity nodeEntity) {
        try (var session = driver.session()) {
            return session.readTransaction(tx -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MATCH (" + nodeEntity.getAliasName() + ":" + nodeEntity.getLabelName() + " " +
                        nodeEntity.getWhere().toJSONString().replaceAll("\"(\\w+)\":", "$1:") + ") \n" +
                        "RETURN COUNT(" + nodeEntity.getAliasName() + ") > 0 AS exists");
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                var result = tx.run(query);
                return result.single().get("exists").asBoolean();
            });
        }
    }

    @Override
    public void delete(NodeEntity nodeEntity) {
        try (var session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MATCH (" + nodeEntity.getAliasName() + ":" + nodeEntity.getLabelName() + " " + nodeEntity.getWhere().toJSONString().replaceAll("\"(\\w+)\":", "$1:") + ")\n" +
                        " DETACH DELETE " + nodeEntity.getAliasName());
                log.debug(" Query {}", stringBuilder);
                var query = new Query(stringBuilder.toString());
                tx.run(query);
            });
        }
    }
}
