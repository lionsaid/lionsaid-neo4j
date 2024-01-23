package com.lionsaid.data.jpa.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lionsaid.data.jpa.entity.Neo4jRelationship;
import com.lionsaid.data.jpa.entity.Neo4jRelationshipHistory;
import com.lionsaid.data.jpa.repository.Neo4jRelationshipHistoryRepository;
import com.lionsaid.data.jpa.repository.Neo4jRelationshipRepository;
import com.lionsaid.data.jpa.service.Neo4jRelationshipService;
import com.lionsaid.data.neo4j.entity.NodeEntity;
import com.lionsaid.data.neo4j.entity.RelationEntity;
import com.lionsaid.data.neo4j.enums.Direction;
import com.lionsaid.data.neo4j.service.Neo4jRelationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class Neo4jRelationshipServiceImpl implements Neo4jRelationshipService {
    private final Neo4jRelationshipRepository neo4jRelationshipRepository;
    private final Neo4jRelationshipHistoryRepository neo4jRelationshipHistoryRepository;
    private final Neo4jRelationService neo4jRelationService;

    @Override
    public void databaseSynchronizationDataToNeo4j() {
        CompletableFuture<Void> resultFuture = CompletableFuture.completedFuture(null);
        Page<Neo4jRelationship> page;
        do {
          page = neo4jRelationshipRepository.findAll(PageRequest.of(0, 1000));
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (Neo4jRelationship o : page.getContent()) {
                CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                    RelationEntity relationEntity = RelationEntity.builder()
                            .source(JSON.parseObject(o.getSource(), NodeEntity.class))
                            .property(JSON.parseObject(o.getProperty()))
                            .id(o.getId())
                            .target(JSON.parseObject(o.getTarget(), NodeEntity.class))
                            .aliasName(o.getAliasName()).name(o.getName()).direction(Direction.valueOf(o.getDirection())).build();
                    JSONObject where = new JSONObject();
                    where.put("id", o.getId());
                    relationEntity.setWhere(where);

                    // 判断数据是否存在
                    if (Boolean.TRUE.equals(neo4jRelationService.exist(relationEntity))) {
                        // 更新数据至neo4j
                        neo4jRelationService.put(relationEntity);
                        // 更新数据log
                        neo4jRelationshipHistoryRepository.updateLastModifiedDateById(LocalDateTime.now(), o.getName() + o.getId());
                    } else {
                        // 保存数据至neo4j
                        neo4jRelationService.post(relationEntity);
                        // 更新数据log
                        neo4jRelationshipHistoryRepository.saveAndFlush(Neo4jRelationshipHistory.builder().id(o.getName() + o.getId()).createdDate(LocalDateTime.now()).relationshipId(o.getId()).relationshipName(o.getName()).build());
                    }
                    return null;
                });

                futures.add(future);
            }

            // 等待所有异步任务完成
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            // 等待所有任务完成后继续循环
            resultFuture = resultFuture.thenCompose((Void) -> allOf);

        } while (!page.isEmpty());

    }
}
