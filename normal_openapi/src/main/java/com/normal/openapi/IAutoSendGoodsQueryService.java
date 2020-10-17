package com.normal.openapi;

import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.autosend.SendGood;

import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
public interface IAutoSendGoodsQueryService {

    List<SendGood> querySendGoods(Map<String, Object> params);

    /**
     * 选品库每日推荐产品查询
     * @param params
     * @return
     */
    List<DailyNoticeItem> queryDailyGoods(Map<String, Object> params);
}
