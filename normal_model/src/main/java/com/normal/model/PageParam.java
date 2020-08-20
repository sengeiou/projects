package com.normal.model;

import java.util.HashMap;

public class PageParam extends HashMap<String, Object> {

    public PageParam() {
        super();
        put("pageNo", 0);
        put("pageSize", 15);
        put("offset", 0);
    }

    public void setPageNo(int pageNo) {
        this.put("pageNo", pageNo);
    }

    public int getPageNo() {
        return (int) get("pageNo");
    }

    public void setPageSize(int pageSize) {
        this.put("pageSize", pageSize);
    }

    public int getPageSize() {
        return (int) get("pageSize");
    }

    @Override
    public Object get(Object key) {
        if ("offset".equals(key)) {
            return  getPageNo() * getPageSize();
        }
        return super.get(key);
    }
}