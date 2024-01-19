package com.lionsaid.data.neo4j.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Slf4j
@Data
@Component
@AllArgsConstructor
public class ReactiveNeo4jConfig {


    @Bean
    public Driver reactiveNeo4jClient() {
        var config = Config.builder()
                .build();
        return GraphDatabase
                .driver("neo4j://localhost:7687", AuthTokens.basic("neo4j", "uranium-nerve-archive-package-dialog-4125"), config);

    }
}
