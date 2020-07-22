package com.normal.biz.order;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("biz")
@Data
public class BizProperties {

    private Integer priceValidMin = 10;
    private double priceChgOffset = 0.01d;

    private int port = 8001;
    private int readTimeout = 60;
    private int workThreadNum = 5;

}
