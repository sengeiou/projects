package com.normal.model.shop;




/**
 * @author: fei.he
 */

public class GoodCat {

    private int id;

    /**
     * 引用的平台类目标识, tb->materialId, pdd->tbMaterialId
     */
    private String referId;

    private String name;

    /**
     * tb or pdd
     */
    private String platform;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}


