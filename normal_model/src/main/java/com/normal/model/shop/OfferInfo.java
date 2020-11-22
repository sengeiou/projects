package com.normal.model.shop;

import com.normal.model.BizDictEnums;


/**
 * @author: fei.he
 * 优惠信息
 */

public class OfferInfo {

    public OfferInfo(String context) {
        this.offerType = BizDictEnums.OFFER_TYPE_MJ;
        this.context = context;
    }

    public OfferInfo(CouponInfo couponInfo) {
        this.offerType = BizDictEnums.OFFER_TYPE_YHQ;
        this.couponInfo = couponInfo;
    }

    /**
     * 优惠类型: 满减/优惠券
      */
    private BizDictEnums offerType;

    /**
     * 除优惠券其他类型优惠信息内容
     */
    private String context;

    /**
     * 优惠券信息
     */
    private CouponInfo couponInfo;

    public BizDictEnums getOfferType() {
        return offerType;
    }

    public void setOfferType(BizDictEnums offerType) {
        this.offerType = offerType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public CouponInfo getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(CouponInfo couponInfo) {
        this.couponInfo = couponInfo;
    }
}
