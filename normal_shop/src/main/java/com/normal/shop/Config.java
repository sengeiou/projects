package com.normal.shop;

import com.normal.base.BaseConfig;
import com.normal.base.query.QueryService;
import com.normal.openapi.OpenApiConfig;
import com.normal.openapi.impl.OpenApiMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author fei.he
 */
@Configuration
@Import({BaseConfig.class, OpenApiConfig.class})
@ComponentScan(basePackages = {
        "com.normal.shop.impl"
})
public class Config implements WebMvcConfigurer {

    @Bean
    public QueryService queryService() {
        return new QueryService();
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new OpenApiMethodArgumentResolver());
    }
}
