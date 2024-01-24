package com.lionsaid.data.neo4j.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 审计配置类
 *
 * @author yaoguohh
 */
@Data
@Component
@ConfigurationProperties(prefix = "neo4j")
public class Neo4jProperties {


    private String url ;
    private String username ;
    private String password ;

}
