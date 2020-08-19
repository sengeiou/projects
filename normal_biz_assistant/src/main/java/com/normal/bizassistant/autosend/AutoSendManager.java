package com.normal.bizassistant.autosend;

import com.normal.bizassistant.BizContext;
import com.normal.bizassistant.BizContextService;
import com.normal.bizassistant.BizContextTypes;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
}
