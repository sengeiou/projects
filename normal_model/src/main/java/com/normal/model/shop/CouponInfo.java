package com.normal.model.shop;



/**
 * @author: fei.he
 */

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

    public String getCouponAmt() {
        return couponAmt;
    }

    public void setCouponAmt(String couponAmt) {
        this.couponAmt = couponAmt;
    }

    public String getValidateDate() {
        return validateDate;
    }

    public void setValidateDate(String validateDate) {
        this.validateDate = validateDate;
    }
}
