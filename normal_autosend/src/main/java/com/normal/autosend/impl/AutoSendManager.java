package com.normal.autosend.impl;

import com.normal.base.biz.BizContextService;
import com.normal.dao.autosend.SendGoodMapper;
import com.normal.model.autosend.SendGood;
import com.normal.model.context.BizContext;
import com.normal.model.context.BizContextTypes;
import com.normal.openapi.IOpenApiService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
@Component
public class AutoSendManager {

    @Autowired
    @Qualifier("taobaoOpenApiService")
    IOpenApiService openApiService;

    @Autowired
    SendGoodMapper sendGoodMapper;

    @Autowired
    Environment environment;

    @Autowired
    BizContextService bizContextService;

    @Transactional
    public List<SendGood> querySendGoods() {
        List<SendGood> unSendGoods = sendGoodMapper.queryUnSendGoods();
        if (CollectionUtils.isEmpty(unSendGoods)) {
            Map<String, Object> params = new HashMap<>(1);
            BizContext context = bizContextService.recoverContext(BizContextTypes.querySendGood);
            if (context == null) {
                params.put("pageNo", 1);
                context = new BizContext();
                context.setType(BizContextTypes.querySendGood);
                context.setContext("2");
            } else {
                String pageNo = context.getContext();
                long next = Long.valueOf(pageNo) + 1;
                params.put("pageNo", next);
                context.setContext(String.valueOf(next));
            }
            bizContextService.insert(context);
            List<SendGood> goods = openApiService.querySendGoods(params);
            sendGoodMapper.batchInsert(goods);
            return goods;
        }
        return unSendGoods;
    }

    public void updateSendGoodsStatus(Integer id) {
        sendGoodMapper.updateSendGoodsStatus(id);
    }

}
