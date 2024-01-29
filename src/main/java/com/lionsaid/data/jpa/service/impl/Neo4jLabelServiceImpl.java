package com.lionsaid.data.jpa.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.lionsaid.data.jpa.entity.Neo4jNode;
import com.lionsaid.data.jpa.entity.Neo4jNodeHistory;
import com.lionsaid.data.jpa.repository.Neo4jLabelRepository;
import com.lionsaid.data.jpa.repository.Neo4jNodeHistoryRepository;
import com.lionsaid.data.jpa.service.Neo4jLabelService;
import com.lionsaid.data.neo4j.entity.NodeEntity;
import com.lionsaid.data.neo4j.service.Neo4jNodeService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class Neo4jLabelServiceImpl implements Neo4jLabelService {
    private final Neo4jLabelRepository neo4jLabelRepository;
    private final Neo4jNodeHistoryRepository neo4jLabelHistoryRepository;
    private final Neo4jNodeService neo4jNodeService;

    @SneakyThrows
    @Override
    public void databaseSynchronizationDataToNeo4j() {
        Page<Neo4jNode> page;
        CompletableFuture<Void> resultFuture = CompletableFuture.completedFuture(null);
        do {
            page = neo4jLabelRepository.findAll(PageRequest.of(0, 200));
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            page.getContent().forEach(o -> {
                CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                    JSONObject where = new JSONObject();
                    where.put("id", o.getId());
                    //判断数据是否存在
                    if (Boolean.TRUE.equals(neo4jNodeService.exist(NodeEntity.builder().labelName(o.getLabelName()).aliasName(o.getAliasName()).where(where).build()))) {
                        //更新数据至neo4j
                        neo4jNodeService.put(NodeEntity.builder().labelName(o.getLabelName()).aliasName(o.getAliasName()).where(where).property(JSONObject.parseObject(o.getProperty())).build());
                        //更新数据log
                        neo4jLabelHistoryRepository.updateLastModifiedDateById(LocalDateTime.now(), o.getLabelName() + o.getId());
                    } else {
                        //保存数据至neo4j
                        neo4jNodeService.post(NodeEntity.builder().labelName(o.getLabelName()).aliasName(o.getAliasName()).property(JSONObject.parseObject(o.getProperty())).build());
                        //更新数据log
                        neo4jLabelHistoryRepository.saveAndFlush(Neo4jNodeHistory.builder().id(o.getLabelName() + o.getId()).createdDate(LocalDateTime.now()).lastModifiedDate(LocalDateTime.now()).labelName(o.getLabelName()).labelId(o.getId()).build());
                    }
                    return null;
                });
                futures.add(future);
            });
            // 等待所有异步任务完成
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            // 等待所有任务完成后继续循环
            // 当所有任务完成时执行操作
            allOf.thenRunAsync(() -> {
                System.out.println("所有任务已完成");
                // 执行其他操作
            });

            // 等待所有任务完成（阻塞操作）
            allOf.join();

            Thread.sleep(3000L);
        } while (!page.isEmpty());
    }
}
