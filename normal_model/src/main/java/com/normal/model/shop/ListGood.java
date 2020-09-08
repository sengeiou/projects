package com.normal.model.shop;

import lombok.Data;

/**
 * @author: fei.he
 */
@Data
public class ListGood {

    private String goodTitle;

    /**
     * 原价
     */
    private String originalPrice;

    /**
     * 当前价格
     */
    private String currPrice;

    /**
     * 优惠描述:可能是优惠券/满减
     */
    private OfferInfo offerInfo;

    /**
     * 主图
     */
    private String  image;

    /**
     * 销量
     */
    private String sellNum;

    /**
     * 是否直接领券
     */
    private boolean direct = false;
}
