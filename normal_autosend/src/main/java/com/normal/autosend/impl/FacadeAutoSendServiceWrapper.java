package com.normal.autosend.impl;

import com.normal.base.ContextSetEvent;
import com.normal.base.biz.BizContextService;
import com.normal.base.utils.ApplicationContextHolder;
import com.normal.model.autosend.SendGood;
import com.normal.openapi.IOpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
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
public class FacadeAutoSendServiceWrapper implements ApplicationListener<ContextSetEvent> {

    @Autowired
    Environment environment;

    @Autowired
    BizContextService bizContextService;

    Map<String, SendGoodQueryStrategy> strategyRegistory = new HashMap<>(8);

    @Autowired
    private IOpenApiService openApiService;

    @Transactional
    public List<SendGood> querySendGoods() {
        String strategyId = environment.getProperty("autosend.strategy");
        SendGoodQueryStrategy strategy = strategyRegistory.get(strategyId);
        return strategy.querySendGoods();
    }
    //todo
    public String queryNotices() {
        return "每日通知:\n 1. xxx 优惠 9:20 开启 \n 2. ddd 优惠开启";
    }

    @Override
    public void onApplicationEvent(ContextSetEvent event) {
        List<SendGoodQueryStrategy> strategies = ApplicationContextHolder.getBeans(SendGoodQueryStrategy.class);
        for (SendGoodQueryStrategy strategy : strategies) {
            strategyRegistory.put(strategy.strategyId(), strategy);
        }
    }
}
