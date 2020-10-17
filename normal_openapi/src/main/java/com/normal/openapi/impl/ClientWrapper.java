package com.normal.openapi.impl;

import com.normal.base.NormalRuntimeException;
import com.normal.base.utils.ApplicationContextHolder;
import com.normal.model.BizCodes;
import com.normal.model.openapi.OpenApiEvent;
import com.pdd.pop.sdk.http.PopBaseHttpRequest;
import com.pdd.pop.sdk.http.PopBaseHttpResponse;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.taobao.api.*;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkTpwdCreateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: fei.he
 */
@Component
public class ClientWrapper {

    public static final Logger logger = LoggerFactory.getLogger(ClientWrapper.class);

    @Autowired
    TaobaoClient taobaoClient;

    @Autowired
    PopHttpClient popHttpClient;

    @Autowired
    Environment environment;

    public <T extends PopBaseHttpResponse> T pddExecute(PopBaseHttpRequest<T> req) {
        try {
            return popHttpClient.syncInvoke(req);
        } catch (Exception e) {
            throw new NormalRuntimeException(e);
        }
    }

    public <T extends TaobaoResponse> T tbExecute(TaobaoRequest<T> req) {
        return tbDoExecute(req);
    }

    private <T extends TaobaoResponse> T tbDoExecute(TaobaoRequest<T> req) {
        T rsp = null;
        try {
            rsp = taobaoClient.execute(req);
            String errorCode = rsp.getSubCode();
            if (!StringUtils.isEmpty(errorCode)) {
                logger.warn("taobao api 查询出错, errorCode: {}, errorMs: {}", errorCode, rsp.getSubMsg());
                if (errorCode.equals(BizCodes.OPEN_API_ERRORCODE_50001)) {
                    ApplicationContextHolder.publishEvent(new OpenApiEvent(BizCodes.OPEN_API_ERRORCODE_50001));
                }
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return rsp;
    }


    public String queryTbPwd(String url) {
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        url = url.replaceAll("\\\\", "");
        url = "https:" + url;
        req.setUrl(url);
        req.setText("优惠券领取");
        req.setUserId(environment.getProperty("openapi.taobao.userid"));
        TbkTpwdCreateResponse rsp = tbExecute(req);
        return rsp.getData().getPasswordSimple();
    }

}
