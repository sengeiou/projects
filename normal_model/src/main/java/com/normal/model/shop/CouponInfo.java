package com.normal.model.shop;

import lombok.Data;

/**
 * @author: fei.he
 */
@Data
public class CouponInfo {

    private String  couponAmt;
    /**
     * 有效区间
     */
    private String  validateDate;

}
