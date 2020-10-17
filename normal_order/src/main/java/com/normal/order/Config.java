package com.normal.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.communicate.server.BizProperties;
import com.normal.communicate.server.BizServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fei.he
 */
@Configuration
@EnableConfigurationProperties(BizProperties.class)
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
