package com.normal.model;

import java.util.HashMap;

public class PageParam extends HashMap<String, Object> {

    public PageParam() {
        super();
        put("pageNo", 0L);
        put("pageSize", 15L);
        put("offset", 0L);
    }

    public void setPageNo(long pageNo) {
        this.put("pageNo", pageNo);
    }

    public long getPageNo() {
        return Long.valueOf(String.valueOf(get("pageNo")));
    }

    public void setPageSize(long pageSize) {
        this.put("pageSize", pageSize);
    }

    public long getPageSize() {
        return Long.valueOf(String.valueOf(get("pageSize")));
    }

    @Override
    public Object get(Object key) {
        if ("offset".equals(key)) {
            return getPageNo() * getPageSize();
        }
        return super.get(key);
    }
}