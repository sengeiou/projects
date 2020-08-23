package com.normal.openapi.impl;

public interface IGoodsTextGenerator {
    /**
     * @param rawObj： open api 返回的原始对象
     * @return
     */
    String text(Object rawObj);
}
