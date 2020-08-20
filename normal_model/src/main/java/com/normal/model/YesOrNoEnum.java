package com.normal.model;

/**
 * @author: fei.he
 */
public enum YesOrNoEnum implements NormalEnum {
    NO(0, "否"),
    YES(1, "是");

    private int key;
    private String value;

    YesOrNoEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int key() {
        return key;
    }

    @Override
    public String value() {
        return value;
    }
}
