package com.normal.core;

public interface NormalEnum {

    int key();

    String value();

    default  NormalEnum valueOf(int key) {
        for (NormalEnum item : this.getClass().getEnumConstants()) {
            if (item.key() == key) {
                return item;
            }
        }
        return null;
    }

}
