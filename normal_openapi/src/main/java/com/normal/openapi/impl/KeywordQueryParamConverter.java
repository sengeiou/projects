package com.normal.openapi.impl;

import com.normal.base.utils.Dates;
import com.normal.model.BizDictEnums;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.CouponInfo;
import com.normal.model.shop.ListGood;
import com.normal.model.shop.OfferInfo;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class KeywordQueryParamConverter implements ParamConvertor<DefaultPageOpenApiQueryParam, TbkDgMaterialOptionalRequest, TbkDgMaterialOptionalResponse, List<ListGood>> {

    private Environment environment;

    public KeywordQueryParamConverter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public TbkDgMaterialOptionalRequest toOpenReq(DefaultPageOpenApiQueryParam myReqParam) {
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("autosend.taobao.adzoneid")));
        req.setQ((String) myReqParam.getOrDefault("keyword", ""));
        //无线平台
        req.setPlatform(2L);
        //排序
        req.setSort(myReqParam.getTaobaoSort());
        req.setPageNo(Long.valueOf(String.valueOf(myReqParam.get("pageNo"))));
        req.setPageSize(Long.valueOf(String.valueOf(myReqParam.get("pageSize"))));
        return req;
    }

    @Override
    public List<ListGood> toMyRes(TbkDgMaterialOptionalResponse openBackParam) {
        List<TbkDgMaterialOptionalResponse.MapData> rawGoods = openBackParam.getResultList();
        List<ListGood> list = rawGoods.stream()
                .map((item) -> {
                    ListGood good = new ListGood();
                    good.setGoodTitle(item.getTitle());
                    good.setItemId(item.getItemId());
                    good.setDirect(false);
                    good.setCurrPrice(item.getReservePrice());
                    good.setOriginalPrice(item.getOrigPrice());
                    if (!StringUtils.isEmpty(item.getCouponInfo())) {
                        good.setOfferInfo(new OfferInfo(item.getCouponInfo()));
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
