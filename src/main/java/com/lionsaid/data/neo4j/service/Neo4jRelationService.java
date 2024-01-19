package com.lionsaid.data.neo4j.service;

import com.lionsaid.data.neo4j.entity.RelationEntity;


public interface Neo4jRelationService {

    void post(RelationEntity relationEntity);

    void put(RelationEntity relationEntity);

    void delete(RelationEntity relationEntity);

    Boolean exist(RelationEntity nodeEntity);
}
