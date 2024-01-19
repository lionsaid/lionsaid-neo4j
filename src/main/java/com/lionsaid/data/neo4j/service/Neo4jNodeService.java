package com.lionsaid.data.neo4j.service;

import com.lionsaid.data.neo4j.entity.NodeEntity;


public interface Neo4jNodeService {
    void post(NodeEntity nodeEntity);

    void put(NodeEntity nodeEntity);

    Boolean exist(NodeEntity nodeEntity);

    void delete(NodeEntity nodeEntity);
}
