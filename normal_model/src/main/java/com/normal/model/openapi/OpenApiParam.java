package com.normal.model.openapi;

import java.util.Map;

/**
 * @author: fei.he
 */
public interface OpenApiParam {
    /**
     * 校验不通过, 直接抛异常
     */
    void check();

    Map<String, Object> getParam();
}
