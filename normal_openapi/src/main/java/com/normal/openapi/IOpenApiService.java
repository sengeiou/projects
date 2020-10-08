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

    List<SendGood> querySendGoods(Map<String, Object> params);

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


    /**
     * 选品库每日推荐产品查询
     * @param params
     * @return
     */
    List<DailyNoticeItem> queryDailyGoods(Map<String, Object> params);




}