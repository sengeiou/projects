package com.normal.openapi.impl.taobao;

import com.normal.model.shop.ItemGood;
import com.normal.openapi.impl.ParamConverter;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkItemInfoGetResponse;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class TaoBaoGoodsDetailQueryParamConverter implements ParamConverter<String, TbkItemInfoGetRequest, TbkItemInfoGetResponse, ItemGood> {


    @Override
    public TbkItemInfoGetRequest toOpenReq(String itemId) {
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(itemId);
        //无线平台
        req.setPlatform(2L);
        return req;
    }

    @Override
    public ItemGood toMyRes(TbkItemInfoGetResponse openBackParam, String itemId) {
        TbkItemInfoGetResponse.NTbkItem item = openBackParam.getResults().get(0);
        ItemGood itemGood = new ItemGood();
        itemGood.setItemId(item.getNumIid());
        itemGood.setDirect(false);
        itemGood.setGoodTitle(item.getTitle());
        itemGood.setImage(item.getPictUrl());
        itemGood.setSellNum(String.valueOf(item.getVolume()));
        itemGood.setPlatform("tb");
        itemGood.setImages(item.getSmallImages()
                .stream()
                .map((image) -> "http:" + image)
                .collect(Collectors.toList()));
        return itemGood;
    }
}
