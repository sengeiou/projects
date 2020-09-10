package com.normal.model.shop;

import lombok.Data;

/**
 * @author: fei.he
 */
@Data
public class CouponInfo {

    private String  couponAmt;
    /**
     * 有效日期区间
     */
    private String  validateDate;

    public CouponInfo(String couponAmt, String validateDate) {
        this.couponAmt = couponAmt;
        this.validateDate = validateDate;
    }
}
