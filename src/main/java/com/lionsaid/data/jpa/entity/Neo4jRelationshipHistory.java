package com.lionsaid.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Neo4jRelationshipHistory", indexes = {
        @Index(name = "idx_neo4jrelationshiphistory", columnList = "relationshipName, relationshipId")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Neo4jRelationshipHistory {
    @CreatedBy
    public String createdBy;
    @CreatedDate
    public LocalDateTime createdDate;
    @LastModifiedBy
    public String lastModifiedBy;
    @LastModifiedDate
    public LocalDateTime lastModifiedDate;
    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    private String relationshipName;
    private String relationshipId;


}
