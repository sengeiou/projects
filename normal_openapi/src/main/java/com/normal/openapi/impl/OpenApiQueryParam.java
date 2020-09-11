package com.normal.openapi.impl;

import java.lang.annotation.*;

/**
 * @author: fei.he
 spring 默认会调用MapMethodProcessor转换类map类型参数

 标记参数用自定义 OpenApiMethodArgumentResolver 解析
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiQueryParam {

}
