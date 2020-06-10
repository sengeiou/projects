package com.normal.portal.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.normal.portal.impl")
@EnableConfigurationProperties(PostProperties.class)
public class Config {

}
