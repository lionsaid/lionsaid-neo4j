package com.lionsaid.data.jpa.repository;

import com.lionsaid.data.jpa.entity.Neo4jNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Neo4jLabelRepository extends JpaRepository<Neo4jNode, String> {
}