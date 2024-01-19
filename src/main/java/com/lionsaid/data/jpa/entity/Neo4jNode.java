package com.lionsaid.data.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Neo4jNode {
    @Id
    private String id;
    private String property;
    private String labelName;
    private String aliasName;
}
