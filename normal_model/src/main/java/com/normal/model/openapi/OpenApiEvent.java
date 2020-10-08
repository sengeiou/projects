package com.normal.model.openapi;

import org.springframework.context.ApplicationEvent;

/**
 * @author: fei.he
 */
public class OpenApiEvent extends ApplicationEvent {

    private String errorCode;

    public OpenApiEvent(String errorCode) {
        super(errorCode);
    }


}
