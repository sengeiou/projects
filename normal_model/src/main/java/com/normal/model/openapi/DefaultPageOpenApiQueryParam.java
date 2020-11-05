package com.normal.model.openapi;

import com.normal.model.PageParam;

/**
 * @author: fei.he
 */
public class DefaultPageOpenApiQueryParam extends PageParam {

    public final static String platform = "platform";

    public final static String keyword = "keyword";

    public final static String defaultCatId = "catId";


    public DefaultPageOpenApiQueryParam() {
        super();
        put("pageSize", 10);
    }


    public DefaultPageOpenApiQueryParam setParam(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public Long getCatId() {
        return getValue(defaultCatId, Long.class);
    }


    public String getPlatform() {
        return getValue(platform, String.class);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        Object obj = get(key);
        if (obj == null  || String.valueOf(obj).length() == 0) {
            return null;
        }
        String value = String.valueOf(obj);
        if (clazz.equals(Long.class)) {
            return (T) Long.valueOf(value);
        }
        if (clazz.equals(Integer.class)) {
            return (T) Integer.valueOf(value);
        }
        return (T) value;
    }

    public void checkNull(String property) {
        if (get(property) == null) {
            throw new IllegalArgumentException("property: " + property + "is not allowed  null");
        }
    }

}
