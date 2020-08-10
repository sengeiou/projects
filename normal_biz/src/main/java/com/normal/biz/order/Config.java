package com.normal.biz.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BizProperties.class)
@MapperScan(basePackages ="com.normal.biz.order")
public class Config {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public IOrderService orderService() {
        return new OrderServiceImpl();
    }

    @Bean
    public PriceGenerator priceGenerator() {
        return new SimplePriceGenerator();
    }

    @Bean
    public BizServer bizServer() {
        return new BizServer();
    }

}
