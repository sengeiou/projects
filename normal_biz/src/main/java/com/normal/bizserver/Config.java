package com.normal.bizserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizserver.order.IOrderService;
import com.normal.bizserver.order.OrderServiceImpl;
import com.normal.bizserver.order.PriceGenerator;
import com.normal.bizserver.order.SimplePriceGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fei.he
 */
@Configuration
@EnableConfigurationProperties(BizProperties.class)
@MapperScan(basePackages ="com.normal.bizserver.order")
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
