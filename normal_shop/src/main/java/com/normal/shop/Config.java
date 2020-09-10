package com.normal.shop;

import com.normal.base.BaseConfig;
import com.normal.base.query.QueryService;
import com.normal.openapi.OpenApiConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fei.he
 */
@Configuration
@Import({BaseConfig.class, OpenApiConfig.class})
@ComponentScan(basePackages = {
        "com.normal.shop.impl"
})
public class Config {

    @Bean
    public QueryService queryService() {
        return new QueryService();
    }
}
