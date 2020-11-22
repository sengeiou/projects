package com.normal.communicate.server;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fei.he
 */
@ConfigurationProperties("biz.server")

public class BizProperties {

    private Integer priceValidMin = 10;
    private double priceChgOffset = 0.01d;

    private int port = 7001;
    private int readTimeout = 60;
    private int workThreadNum = 5;
    private String priceQrCode = "statics/default.png";

    public Integer getPriceValidMin() {
        return priceValidMin;
    }

    public void setPriceValidMin(Integer priceValidMin) {
        this.priceValidMin = priceValidMin;
    }

    public double getPriceChgOffset() {
        return priceChgOffset;
    }

    public void setPriceChgOffset(double priceChgOffset) {
        this.priceChgOffset = priceChgOffset;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWorkThreadNum() {
        return workThreadNum;
    }

    public void setWorkThreadNum(int workThreadNum) {
        this.workThreadNum = workThreadNum;
    }

    public String getPriceQrCode() {
        return priceQrCode;
    }

    public void setPriceQrCode(String priceQrCode) {
        this.priceQrCode = priceQrCode;
    }
}
