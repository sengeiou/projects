package com.normal.resources.impl;

import com.normal.core.mybatis.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config {

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost")
                        .allowedMethods("GET", "POST")
                        .allowCredentials(false).maxAge(3600);

            }
        };
    }
}
