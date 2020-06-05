package com.normal.resources.impl;

/**
 * trd_resource
 * 
 * @author
 */
public class Resource  {
    private Integer id;


    private String resName;

    private String realUrl;

    /**
     * 每个bit代表一种标签
     */
    private ResourceBitLabels resLabels;

    /**
     * 0-不可用
        1-可用
     */
    private Byte status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public ResourceBitLabels getResLabels() {
        return resLabels;
    }

    public void setResLabels(ResourceBitLabels resLabels) {
        this.resLabels = resLabels;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}