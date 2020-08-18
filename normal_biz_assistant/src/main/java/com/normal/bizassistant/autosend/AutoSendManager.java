package com.normal.bizassistant.autosend;

import com.normal.bizassistant.BizContext;
import com.normal.bizassistant.BizContextService;
import com.normal.bizassistant.BizContextTypes;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
@Component
public class AutoSendManager {

    @Autowired
    IOpenApiService openApiService;

    @Autowired
    SendGoodMapper sendGoodMapper;

    @Autowired
    Environment environment;

    @Autowired
    BizContextService bizContextService;

    public List<SendGood> querySendGoods() {
        List<SendGood> unSendGoods = sendGoodMapper.queryUnSendGoods();
        if (CollectionUtils.isEmpty(unSendGoods)) {
            Map<String, Object> params = new HashMap<>(1);
            BizContext context = bizContextService.recoverContext(BizContextTypes.querySendGood);
            if (context == null) {
                params.put("pageNo", 1);
            }else {
                String pageNo = context.getContext();
                params.put("pageNo", Long.valueOf(pageNo));
            }
            return openApiService.querySendGoods(params);
        }
        return unSendGoods;
    }
}
