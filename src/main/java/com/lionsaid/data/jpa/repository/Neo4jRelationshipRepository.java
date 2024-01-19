package com.lionsaid.data.jpa.repository;

import com.lionsaid.data.jpa.entity.Neo4jRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Neo4jRelationshipRepository extends JpaRepository<Neo4jRelationship, String> {
}