package com.normal.openapi.impl;

/**
 * @author: fei.he
 */
public abstract class OpenApiModelConvertor<S, T> {

    public abstract T convert(S s);



}
