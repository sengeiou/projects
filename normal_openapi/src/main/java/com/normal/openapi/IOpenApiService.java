package com.normal.openapi;

import com.normal.base.mybatis.Page;
import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.autosend.SendGood;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.ItemGood;
import com.normal.model.shop.ListGood;

import java.util.List;
import java.util.Map;

public interface IOpenApiService {

    /**
     * 查商品列表
     * @param param
     * @return
     */
    Page<ListGood> pageQueryGoods(DefaultPageOpenApiQueryParam param);

    /**
     * 查商品详情
     * @param itemId
     * @return
     */
    ItemGood queryItemGood(String itemId);



}