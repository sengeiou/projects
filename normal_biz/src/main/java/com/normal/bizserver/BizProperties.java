package com.normal.bizserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fei.he
 */
@ConfigurationProperties("biz.server")
@Data
public class BizProperties {

    private Integer priceValidMin = 10;
    private double priceChgOffset = 0.01d;

    private int port = 7001;
    private int readTimeout = 60;
    private int workThreadNum = 5;
    private String priceQrCode = "statics/default.png";

}
