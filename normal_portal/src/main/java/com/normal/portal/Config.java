package com.normal.portal;

import com.normal.base.mybatis.PageInterceptor;
import com.normal.base.web.PageTemplateDirectiveModel;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
@MapperScan(basePackages = "com.normal.portal.impl")
@EnableConfigurationProperties(PostProperties.class)
@AutoConfigureAfter(FreeMarkerAutoConfiguration.class)
public class Config implements WebMvcConfigurer {
    public static final Logger logger = LoggerFactory.getLogger(Config.class);

    @Autowired
    freemarker.template.Configuration configuration;

    @Autowired
    PostProperties postProperties;

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

        String schema = System.getProperty("os.name").startsWith("Windows") ? "file:///" : "file:";
        logger.info("resourceLocations: {}", schema + postProperties.getUploadDir());
        registry.addResourceHandler("/static/**")
                .addResourceLocations(schema + postProperties.getUploadDir() + File.separator)
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
    }


}
