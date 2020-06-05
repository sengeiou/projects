package com.normal.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class Refacts {

    private static Logger log = LoggerFactory.getLogger(Refacts.class);
    public static void setField(Object obj, String fieldName, Object value) {
        
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            log.error("e", e);
        }  
     }
}