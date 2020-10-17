package com.normal.base.utils;

import com.normal.base.ContextSetEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        synchronized (this) {
            if (ApplicationContextHolder.context == null) {
                ApplicationContextHolder.context = applicationContext;
            }
        }
        context.publishEvent(new ContextSetEvent("contextSet"));
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> List<T> getBeans(Class<T> clazz) {
        String[] names = context.getBeanNamesForType(clazz);
        List rst = Stream.of(names)
                .map((name) -> context.getBean(name))
                .collect(Collectors.toList());
        return rst;
    }

    public static <T> T getBean(String qualifier, Class<T> clazz) {
        return context.getBean(qualifier, clazz);
    }

    public static ApplicationContext getContext() {
        return context;
    }


    public static void publishEvent(ApplicationEvent event) {
        context.publishEvent(event);
    }


}