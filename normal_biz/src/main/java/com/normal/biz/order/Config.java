package com.normal.biz.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BizProperties.class)
public class Config {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
