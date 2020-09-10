package com.normal.model;

/**
 * @author: fei.he
 */
public enum YesOrNoEnum implements NormalEnum {
    NO("0", "否"),
    YES("1", "是");

    private String key;
    private String value;

    YesOrNoEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String value() {
        return value;
    }
}
