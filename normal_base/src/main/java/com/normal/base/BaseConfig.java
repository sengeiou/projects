package com.normal.base;

import com.normal.base.utils.ApplicationContextHolder;
import com.normal.dao.DaoConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author: fei.he
 */
@Configuration
@Import(DaoConfig.class)
@ComponentScan(basePackages = {"com.normal.base"})
public class BaseConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ApplicationContextHolder holder() {
        return new ApplicationContextHolder();
    }
}
