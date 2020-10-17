package com.normal.base.cache;

import java.util.*;

/**
 * @author: fei.he
 */
public class HashMapCache implements Cache {

    private int maxSize;

    private Map<String, Object> innerCache;

    private Queue<String> keys;

    public HashMapCache(int maxSize) {
        keys = new LinkedList<>();
        this.maxSize = maxSize;
        this.innerCache = new HashMap<>(maxSize);
    }

    @Override
    public void put(String key, Object o) {
        if (innerCache.size() == maxSize) {
            innerCache.remove(keys.poll());
        }
        keys.offer(key);
        this.innerCache.put(key, o);
    }

    @Override
    public Optional get(String key) {
        return Optional.ofNullable(innerCache.get(key));
    }
}
