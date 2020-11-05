package com.normal.openapi.impl.pdd;

import com.normal.base.mybatis.Page;
import com.normal.model.BizDictEnums;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.openapi.PddOpenApiQueryParam;
import com.normal.model.shop.CouponInfo;
import com.normal.model.shop.ListGood;
import com.normal.model.shop.OfferInfo;
import com.normal.openapi.impl.ClientWrapper;
import com.normal.openapi.impl.ParamConverter;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsDetailRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsDetailResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
@Component
public class PddListGoodParamConverter implements ParamConverter<PddOpenApiQueryParam, PddDdkGoodsSearchRequest, PddDdkGoodsSearchResponse, Page<ListGood>> {

    @Autowired
    private ClientWrapper clientWrapper;

    @Autowired
    private Environment environment;

    @Override
    public PddDdkGoodsSearchRequest toOpenReq(PddOpenApiQueryParam myReqParam) {
        PddDdkGoodsSearchRequest req = new PddDdkGoodsSearchRequest();
        req.setCatId(myReqParam.getCatId());
        String keyword = myReqParam.getValue(PddOpenApiQueryParam.keyword, String.class);
        req.setKeyword(keyword);
        req.setPid(environment.getProperty("openapi.pdd.pid").replace("|", "_"));
        Integer sortType = myReqParam.getValue(PddOpenApiQueryParam.sort, Integer.class);
        sortType = sortType == null ? 0 : sortType;
        req.setSortType(sortType);
        req.setPage(Long.valueOf(myReqParam.getPageNo()).intValue());
        req.setPageSize(Long.valueOf(myReqParam.getPageSize()).intValue());
        return req;
    }

    @Override
    public Page<ListGood> toMyRes(PddDdkGoodsSearchResponse openBackParam, PddOpenApiQueryParam myReqParam) {
        PddDdkGoodsSearchResponse.GoodsSearchResponse rsp = openBackParam.getGoodsSearchResponse();
        List<Long> goodIds = rsp.getGoodsList().stream()
                .map(item -> item.getGoodsId()).collect(Collectors.toList());
        if (goodIds.isEmpty()) {
            return new Page<>();
        }

        List<ListGood> rst = goodIds.stream().map((goodsId) -> {

            PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
            request.setGoodsIdList(Arrays.asList(goodsId));
            request.setPid(environment.getProperty("openapi.pdd.pid").replace("|", "_"));
            request.setSearchId(rsp.getSearchId());

            PddDdkGoodsDetailResponse detailsRsp = clientWrapper.pddExecute(request);
            List<ListGood> collect = detailsRsp.getGoodsDetailResponse().getGoodsDetails().stream()
                    .map(detail -> {
                        BigDecimal yb = BigDecimal.valueOf(100);
                        BigDecimal couponDiscount = BigDecimal.valueOf(detail.getCouponDiscount()).divide(yb);
                        BigDecimal minGroupPrice = BigDecimal.valueOf(detail.getMinGroupPrice()).divide(yb);
                        ListGood good = new ListGood();
                        good.setItemId(detail.getGoodsId());
                        good.setGoodTitle(detail.getGoodsName());

                        BigDecimal discount = couponDiscount == null ? BigDecimal.ZERO : couponDiscount;
                        good.setImage(detail.getGoodsImageUrl());
                        good.setSellNum(detail.getSalesTip());
                        good.setPlatform(BizDictEnums.PLATFORM_PDD.key());

                        if (detail.getHasCoupon()) {
                            good.setOriginalPrice(String.valueOf(minGroupPrice));
                            good.setOfferInfo(new OfferInfo(new CouponInfo(discount.toString(), "")));
                            good.setCurrPrice(minGroupPrice.subtract(discount).toString());
                        } else {
                            good.setOfferInfo(new OfferInfo("只售" + minGroupPrice));
                            good.setCurrPrice(minGroupPrice.toString());
                        }
                        return good;
                    })
                    .collect(Collectors.toList());
            return collect.get(0);
        }).collect(Collectors.toList());

        return new Page<>(rst, myReqParam.getPageNo(), Long.valueOf(rsp.getTotalCount()));
    }
}
