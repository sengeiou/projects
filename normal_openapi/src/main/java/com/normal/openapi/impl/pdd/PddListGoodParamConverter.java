package com.normal.openapi.impl.pdd;

import com.normal.base.mybatis.Page;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
@Component
public class PddListGoodParamConverter implements ParamConverter<DefaultPageOpenApiQueryParam, PddDdkGoodsSearchRequest, PddDdkGoodsSearchResponse, Page<ListGood>> {

    @Autowired
    private ClientWrapper clientWrapper;

    @Autowired
    private Environment environment;

    @Override
    public PddDdkGoodsSearchRequest toOpenReq(DefaultPageOpenApiQueryParam myReqParam) {
        PddDdkGoodsSearchRequest req = new PddDdkGoodsSearchRequest();
        req.setCatId(myReqParam.getValue(DefaultPageOpenApiQueryParam.catId, Long.class));
        String keyword = myReqParam.getValue(DefaultPageOpenApiQueryParam.keyword, String.class);
        if (StringUtils.isEmpty(keyword)) {
            req.setKeyword(keyword);
        }
        Integer sortType = myReqParam.getValue(DefaultPageOpenApiQueryParam.pddSort, Integer.class);
        sortType = sortType == null ? 0 : sortType;
        req.setSortType(sortType);
        req.setPage(Long.valueOf(myReqParam.getPageNo()).intValue());
        req.setPageSize(Long.valueOf(myReqParam.getPageSize()).intValue());
        return req;
    }

    @Override
    public Page<ListGood> toMyRes(PddDdkGoodsSearchResponse openBackParam, DefaultPageOpenApiQueryParam myReqParam) {
        PddDdkGoodsSearchResponse.GoodsSearchResponse rsp = openBackParam.getGoodsSearchResponse();
        List<Long> goodIds = rsp.getGoodsList().stream()
                .map(item -> item.getGoodsId()).collect(Collectors.toList());
        if (goodIds.isEmpty()) {
            return new Page<>();
        }

        List<ListGood> rst = goodIds.stream().map((goodsId) -> {

            PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
            request.setGoodsIdList(Arrays.asList(goodsId));
            request.setPid(environment.getProperty("openapi.pdd.pid"));
            request.setSearchId(rsp.getSearchId());

            PddDdkGoodsDetailResponse detailsRsp = clientWrapper.pddExecute(request);
            List<ListGood> collect = detailsRsp.getGoodsDetailResponse().getGoodsDetails().stream()
                    .map(detail -> {
                        ListGood good = new ListGood();
                        good.setItemId(detail.getGoodsId());
                        good.setGoodTitle(detail.getGoodsName());
                        String originalPrice = String.valueOf(detail.getMinGroupPrice() / 100);
                        good.setOriginalPrice(originalPrice);
                        long discount = detail.getCouponDiscount() == null ? 0 : detail.getCouponDiscount() / 100;
                        good.setCurrPrice(String.valueOf((Long.valueOf(originalPrice) - discount)));
                        good.setImage(detail.getGoodsImageUrl());
                        good.setSellNum(detail.getSalesTip());
                        good.setOfferInfo(new OfferInfo(new CouponInfo(String.valueOf(discount), "")));
                        return good;
                    })
                    .collect(Collectors.toList());
            return collect.get(0);
        }).collect(Collectors.toList());

        return new Page<>(rst, myReqParam.getPageNo(), Long.valueOf(rsp.getTotalCount()));
    }
}
