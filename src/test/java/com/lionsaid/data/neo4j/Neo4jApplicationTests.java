package com.lionsaid.data.neo4j;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Neo4jApplicationTests {

    @Test
    void contextLoads() {
            String jsonString = "{\"name\":\"zhangsan\",\"age\":23}";

            // 使用正则表达式去掉键名外的双引号
            String modifiedJsonString = jsonString.replaceAll("\"(\\w+)\":", "$1:");

            System.out.println(modifiedJsonString);

    }

}
