package com.normal.base;

import com.normal.base.utils.ApplicationContextHolder;
import com.normal.dao.DaoConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: fei.he
 */
@Configuration
@Import(DaoConfig.class)
@ComponentScan(basePackages = {"com.normal.base.biz"})
public class BaseConfig {

    @Bean
    public ApplicationContextHolder holder() {
        return new ApplicationContextHolder();
    }
}
