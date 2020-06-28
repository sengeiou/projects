package com.normal.portal.impl;

import com.normal.core.mybatis.PageInterceptor;
import com.normal.core.web.PageTemplateDirectiveModel;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sun.tools.jar.resources.jar;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
@MapperScan(basePackages = "com.normal.portal.impl")
@EnableConfigurationProperties(PostProperties.class)
@AutoConfigureAfter(FreeMarkerAutoConfiguration.class)
public class Config implements WebMvcConfigurer {
    public static final Logger logger = LoggerFactory.getLogger(Config.class);

     @Autowired
    freemarker.template.Configuration configuration;

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

    @PostConstruct
    public void initFreeMarkerConfig() {
        configuration.setSharedVariable("page", new PageTemplateDirectiveModel());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String jarLocation =  this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        logger.info("jar location: {}", jarLocation);
        registry.addResourceHandler("/static/**").addResourceLocations(jarLocation + "static");
    }


}
