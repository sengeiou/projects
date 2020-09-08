package com.normal.openapi;

import com.normal.model.autosend.SendGood;
import com.normal.model.openapi.OpenApiParam;
import com.normal.model.openapi.PageOpenApiGoodQueryParam;
import com.normal.model.shop.ItemGood;
import com.normal.model.shop.ListGood;

import java.util.List;
import java.util.Map;

public interface IOpenApiService {

    List<SendGood> querySendGoods(Map<String, Object> params);

    /**
     * 个性化查询列表
     *
     * @return
     */
    List<ListGood> pageCustomeQueryGoods(PageOpenApiGoodQueryParam param);


    /**
     * 查询商品详情
     * @param param
     * @return
     */
    ItemGood queryItemGood(OpenApiParam param);



}