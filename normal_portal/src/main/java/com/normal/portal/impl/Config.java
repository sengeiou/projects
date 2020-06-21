package com.normal.portal.impl;

import com.normal.core.web.PageTemplateDirectiveModel;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@MapperScan(basePackages = "com.normal.portal.impl")
@EnableConfigurationProperties(PostProperties.class)
@AutoConfigureAfter(FreeMarkerAutoConfiguration.class)
public class Config {

     @Autowired
    freemarker.template.Configuration configuration;

    @PostConstruct
    public void initFreeMarkerConfig() {
        configuration.setSharedVariable("page", new PageTemplateDirectiveModel());
    }

}
