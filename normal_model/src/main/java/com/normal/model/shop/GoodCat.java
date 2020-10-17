package com.normal.model.shop;

import lombok.Data;


/**
 * @author: fei.he
 */
@Data
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
 }
