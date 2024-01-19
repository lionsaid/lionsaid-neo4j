package com.lionsaid.data.jpa.repository;

import com.lionsaid.data.jpa.entity.Neo4jNodeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface Neo4jNodeHistoryRepository extends JpaRepository<Neo4jNodeHistory, String> {
    @Transactional
    @Modifying
    @Query("update Neo4jNodeHistory n set n.lastModifiedDate = ?1 where n.id = ?2")
    void updateLastModifiedDateById(LocalDateTime lastModifiedDate, String id);
}