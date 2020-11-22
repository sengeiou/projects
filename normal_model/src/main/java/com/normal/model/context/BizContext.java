package com.normal.model.context;



/**
 * @author: fei.he
 */

public class BizContext {

    private Integer id;
    /**
     * BizContextTypes
     */
    private String type;

    private String context;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
