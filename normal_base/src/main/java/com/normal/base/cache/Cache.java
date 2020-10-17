package com.normal.base.cache;

import java.util.Optional;

/**
 * @author: fei.he
 */
public interface Cache {

    void put(String key, Object t);

    Optional get(String key);

}