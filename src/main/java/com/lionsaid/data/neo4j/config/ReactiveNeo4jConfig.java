package com.lionsaid.data.neo4j.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Slf4j
@Data
@Component
@AllArgsConstructor
@EnableConfigurationProperties({Neo4jProperties.class})
public class ReactiveNeo4jConfig {

  private final   Neo4jProperties neo4jProperties;
    @Bean
    public Driver reactiveNeo4jClient() {
        log.info("neo4jProperties {}",neo4jProperties.getUrl());
        var config = Config.builder()
                .build();
        return GraphDatabase
                .driver(neo4jProperties.getUrl(), AuthTokens.basic(neo4jProperties.getUsername(), neo4jProperties.getPassword()), config);

    }
}
