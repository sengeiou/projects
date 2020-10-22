package com.normal.openapi.impl;

import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Iterator;

/**
 * @author: fei.he
 */
public class OpenApiMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(DefaultPageOpenApiQueryParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        DefaultPageOpenApiQueryParam param = new DefaultPageOpenApiQueryParam();
        Iterator<String> parameterNames = webRequest.getParameterNames();
        for (; parameterNames.hasNext(); ) {
            String next = parameterNames.next();
            param.put(next, webRequest.getParameter(next));
        }
        return param;
    }
}
