package com.normal.openapi.impl;

import com.normal.model.shop.ItemGood;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkItemInfoGetResponse;

import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class GoodsDetailQueryParamConverter implements ParamConverter<String, TbkItemInfoGetRequest, TbkItemInfoGetResponse, ItemGood> {


    @Override
    public TbkItemInfoGetRequest toOpenReq(String itemId) {
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(itemId);
        //无线平台
        req.setPlatform(2L);
        return req;
    }

    @Override
    public ItemGood toMyRes(TbkItemInfoGetResponse openBackParam) {
        TbkItemInfoGetResponse.NTbkItem item = openBackParam.getResults().get(0);
        ItemGood itemGood = new ItemGood();
        itemGood.setItemId(item.getNumIid());
        itemGood.setDirect(false);
        itemGood.setGoodTitle(item.getTitle());
        itemGood.setImage(item.getPictUrl());
        itemGood.setSellNum(String.valueOf(item.getVolume()));
        itemGood.setImages(item.getSmallImages()
                .stream()
                .map((image) -> "http:" + image)
                .collect(Collectors.toList()));
        return itemGood;
    }
}
