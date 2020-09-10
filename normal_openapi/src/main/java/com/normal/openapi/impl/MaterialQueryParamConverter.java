package com.normal.openapi.impl;

import com.normal.base.utils.Dates;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.CouponInfo;
import com.normal.model.shop.ListGood;
import com.normal.model.shop.OfferInfo;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class MaterialQueryParamConverter implements ParamConvertor<Map<String, Object>, TbkDgOptimusMaterialRequest, TbkDgOptimusMaterialResponse, List<ListGood>> {

    private Environment environment;

    public MaterialQueryParamConverter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public TbkDgOptimusMaterialRequest toOpenReq(Map<String, Object> myReqParam) {
        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("autosend.taobao.adzoneid")));
        Object materialId = myReqParam.get("materialId");
        if (materialId != null) {
            req.setMaterialId(Long.valueOf(String.valueOf(materialId)));
        }
        if (myReqParam instanceof DefaultPageOpenApiQueryParam) {
            req.setMaterialId(Long.valueOf(((DefaultPageOpenApiQueryParam) myReqParam).getQueryType().key()));
        }
        req.setPageNo(Long.valueOf(String.valueOf(myReqParam.get("pageNo"))));
        req.setPageSize(Long.valueOf(String.valueOf(myReqParam.get("pageSize"))));
        return req;
    }

    @Override
    public List<ListGood> toMyRes(TbkDgOptimusMaterialResponse openBackParam) {
        List<TbkDgOptimusMaterialResponse.MapData> rawGoods = openBackParam.getResultList();
        List<ListGood> list = rawGoods.stream()
                .map((item) -> {
                    ListGood good = new ListGood();
                    good.setItemId(item.getItemId());
                    good.setGoodTitle(item.getTitle());
                    good.setDirect(false);
                    good.setCurrPrice(item.getReservePrice());
                    good.setOriginalPrice(item.getOrigPrice());
                    if (!StringUtils.isEmpty(item.getPromotionInfo())) {
                        good.setOfferInfo(new OfferInfo(item.getPromotionInfo()));
                    } else {
                        String validateRange = Dates.format(Long.valueOf(item.getCouponStartTime())) + "~" + Dates.format(Long.valueOf(item.getCouponEndTime()));
                        good.setOfferInfo(new OfferInfo(new CouponInfo(String.valueOf(item.getCouponAmount()), validateRange)));
                    }
                    good.setImage("http:" + item.getPictUrl());
                    good.setSellNum(String.valueOf(item.getSellNum()));
                    return good;
                })
                .collect(Collectors.toList());
        return list;
    }
}
