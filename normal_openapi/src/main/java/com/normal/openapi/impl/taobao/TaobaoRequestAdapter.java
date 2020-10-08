package com.normal.openapi.impl.taobao;

import com.taobao.api.ApiRuleException;
import com.taobao.api.BaseTaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.internal.util.TaobaoHashMap;

import java.util.Map;

/**
 * @author: fei.he
 */
public abstract class TaobaoRequestAdapter<T extends TaobaoResponse> extends BaseTaobaoRequest<T> {


    @Override
    public Map<String, String> getTextParams() {
        return getParams();
    }


    @Override
    public Class<T> getResponseClass() {
        return getResponseClazz();
    }


    @Override
    public void check() throws ApiRuleException {

    }

    protected abstract TaobaoHashMap getParams();

    protected abstract Class<T> getResponseClazz();
}
