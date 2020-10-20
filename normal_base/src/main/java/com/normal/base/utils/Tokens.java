package com.normal.base.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author: fei.he
 */
public class Tokens {

    private final static String secret = "mDn!p*w90~m";

    private Map<String, String> payload = new HashMap<>();

    public String gen() {
        StringJoiner joiner = new StringJoiner(";");
        for (Map.Entry<String, String> entry : this.payload.entrySet()) {
            joiner.add(entry.getKey() + ":" + entry.getValue());
        }
    return null;
    }

    public Tokens addPayload(String key, String value) {
        this.payload.put(key, value);
        return this;
    }


}
