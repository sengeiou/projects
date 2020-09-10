package com.normal.model.shop;

import com.normal.model.BizDictEnums;
import lombok.Data;

/**
 * @author: fei.he
 * 优惠信息
 */
@Data
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


}
