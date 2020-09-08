package com.normal.model.shop;

import lombok.Data;

import java.util.List;

/**
 * @author: fei.he
 */
@Data
public class ItemGood extends ListGood{

    private List<String> images;

    /**
     * 优惠券信息
     */
    private CouponInfo couponInfo;
}
