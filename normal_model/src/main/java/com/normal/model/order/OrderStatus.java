package com.normal.model.order;

import com.normal.model.NormalEnum;

public enum OrderStatus implements NormalEnum {

    NEW("0", "新建"),
    PAIED("1", "已支付"),
    TIMEOUT("2", "超时未付"),
    CLOSED("3", "已关闭");

    private String key;
    private String value;

    OrderStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }


    @Override
    public String key() {
        return this.key;
    }

    @Override
    public String value() {
        return this.value;
    }


}
