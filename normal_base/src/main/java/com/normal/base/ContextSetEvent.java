package com.normal.base;

import org.springframework.context.ApplicationEvent;

/**
 * @author: fei.he
 */
public class ContextSetEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ContextSetEvent(Object source) {
        super(source);
    }
}
