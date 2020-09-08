package com.normal.openapi.impl;

import com.taobao.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: fei.he
 */
@Component
public class TaobaoClientWrapper {
    public static final Logger logger = LoggerFactory.getLogger(TaobaoClientWrapper.class);

    @Autowired
    TaobaoClient taobaoClient;


    public <T extends TaobaoResponse> T execute(TaobaoRequestAdapter<T> req) {
        return doExecute(req);
    }

    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> req) {
        return doExecute(req);
    }

    private <T extends TaobaoResponse> T doExecute(TaobaoRequest<T> req) {
        T rsp = null;
        try {
            rsp = taobaoClient.execute(req);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return rsp;
    }
}
