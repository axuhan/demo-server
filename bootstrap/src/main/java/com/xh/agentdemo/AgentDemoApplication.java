package com.xh.agentdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(
        scanBasePackages = "com.xh.*",
        exclude = {
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
        }
)
@ImportResource({
        "classpath*:spring-*.xml",
        "classpath*:*-datasource.xml",
})
@EnableCaching
@EnableWebMvc
public class AgentDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentDemoApplication.class, args);
    }

}