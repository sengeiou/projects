package com.normal.openapi.impl.pdd;

import com.normal.base.mybatis.Page;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.ItemGood;
import com.normal.model.shop.ListGood;
import com.normal.openapi.IOpenApiService;
import com.normal.openapi.impl.ClientWrapper;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.taobao.api.internal.toplink.protocol.NotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author: fei.he
 */
@Component
@Qualifier("pddOpenApiService")
public class PddOpenApiServiceImpl implements IOpenApiService {

    @Autowired
    ClientWrapper clientWrapper;

    @Autowired
    PddListGoodParamConverter pddListGoodParamConverter;

    @Override
    public Page<ListGood> pageQueryGoods(DefaultPageOpenApiQueryParam param) {
        PddDdkGoodsSearchRequest req = pddListGoodParamConverter.toOpenReq(param);
        PddDdkGoodsSearchResponse rsp = clientWrapper.pddExecute(req);
        return pddListGoodParamConverter.toMyRes(rsp, param);
    }

    @Override
    public ItemGood queryItemGood(String itemId) {
        throw new UnsupportedOperationException("pdd open api not need item query");
    }
}
