package com.normal.devtool.dbupdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 * 数据库对象: 表,列,视图..
 */
public abstract class DbObj {

    private String name;

    private Map<String, Object> attributes = new HashMap<>(2);

    private List<DbObj> children = new ArrayList<>();



    public void addChild(DbObj child){
        children.add(child);
    }

    abstract List<DbObj> getChildren(Class<? extends DbObj> clazz);



}
