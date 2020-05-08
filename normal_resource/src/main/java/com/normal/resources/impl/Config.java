package com.normal.resources.impl;

import com.normal.core.mybatis.PageInterceptor;

import org.apache.ibatis.session.SqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public PageInterceptor pageInterceptor() {

        return new PageInterceptor();
    }


}
