package com.normal.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        synchronized (this) {
            if (ApplicationContextHolder.context == null) {
                ApplicationContextHolder.context = applicationContext;
            }
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean(String qualifier, Class<T> clazz) {
        return context.getBean(qualifier, clazz);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}