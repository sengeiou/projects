package com.normal.core.mybatis;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PageParam {
    private int pageNo = 0; 
    private int pageSize = 15;
    private Map<String, Object> param = new HashMap<String,Object>();
    

    public int getOffset(){
        return this.pageNo * this.pageSize;
    }

}