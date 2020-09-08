package com.normal.model.shop;

import com.fasterxml.jackson.annotation.JsonValue;
import com.normal.model.BizDictEnums;
import lombok.Data;

/**
 * @author: fei.he
 * 优惠信息
 */
@Data
public class OfferInfo {
    /**
     * 优惠类型: 满减/优惠券
      */
    private BizDictEnums offerType;

    @JsonValue
    private String context;

}
