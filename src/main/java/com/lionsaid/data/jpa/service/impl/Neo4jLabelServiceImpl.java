package com.lionsaid.data.jpa.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.lionsaid.data.jpa.entity.Neo4jNode;
import com.lionsaid.data.jpa.entity.Neo4jNodeHistory;
import com.lionsaid.data.jpa.repository.Neo4jNodeHistoryRepository;
import com.lionsaid.data.jpa.repository.Neo4jLabelRepository;
import com.lionsaid.data.jpa.service.Neo4jLabelService;
import com.lionsaid.data.neo4j.entity.NodeEntity;
import com.lionsaid.data.neo4j.service.Neo4jNodeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class Neo4jLabelServiceImpl implements Neo4jLabelService {
    private final Neo4jLabelRepository neo4jLabelRepository;
    private final Neo4jNodeHistoryRepository neo4jLabelHistoryRepository;
    private final Neo4jNodeService neo4jNodeService;

    @Override
    public void databaseSynchronizationDataToNeo4j() {
        Page<Neo4jNode> page;
        do {
            page = neo4jLabelRepository.findAll(PageRequest.of(0, 1000));
            page.getContent().forEach(o -> {
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
                    neo4jLabelHistoryRepository.saveAndFlush(Neo4jNodeHistory.builder().id(o.getLabelName() + o.getId()).createdDate(LocalDateTime.now()).labelName(o.getLabelName()).labelId(o.getId()).build());
                }
            });
        } while (!page.isEmpty());
    }
}
