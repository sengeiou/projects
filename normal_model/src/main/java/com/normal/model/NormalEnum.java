package com.normal.model;

/**
 * @author fei.he
 */
public interface NormalEnum {

    String key();

    String value();

    default NormalEnum valueOf(int key) {
        for (NormalEnum item : this.getClass().getEnumConstants()) {
            if (item.key().equals( key)) {
                return item;
            }
        }
        return null;
    }

}
