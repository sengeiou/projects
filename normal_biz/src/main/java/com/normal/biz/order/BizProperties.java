package com.normal.biz.order;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("biz")
@Data
public class BizProperties {

    private Integer priceValidMin = 10;
    private double  priceChgOffset = 0.01f;
}
