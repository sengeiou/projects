package com.normal.openapi.impl;

import com.normal.base.mybatis.Page;
import com.normal.model.BizDictEnums;
import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.autosend.SendGood;
import com.normal.model.openapi.PddOpenApiQueryParam;
import com.normal.model.openapi.TbOpenApiQueryParam;
import com.normal.model.shop.ItemGood;
import com.normal.model.shop.ListGood;
import com.normal.openapi.impl.pdd.PddListGoodParamConverter;
import com.normal.openapi.impl.taobao.*;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author: fei.he
 */
@Component
public class OpenApiManager {

    @Autowired
    ClientWrapper clientWrapper;

    @Autowired
    Environment environment;

    private TaoBaoMaterialQueryParamConverter materialParamConverter;
    private TaoBaoKeywordQueryParamConverter keywordQueryParamConverter;
    private TaoBaoGoodsDetailQueryParamConverter goodsDetailQueryParamConverter;
    private TaoBaoSendGoodResultParamConverter sendGoodResultParamConverter;
    private TaoBaoDailyNoticeQueryParamConverter dailyNoticeQueryParamConverter;


    @Autowired
    PddListGoodParamConverter pddListGoodParamConverter;


    @PostConstruct
    public void init() {
        materialParamConverter = new TaoBaoMaterialQueryParamConverter(environment, clientWrapper);
        keywordQueryParamConverter = new TaoBaoKeywordQueryParamConverter(environment);
        goodsDetailQueryParamConverter = new TaoBaoGoodsDetailQueryParamConverter();
        sendGoodResultParamConverter = new TaoBaoSendGoodResultParamConverter(environment, clientWrapper, materialParamConverter);
        dailyNoticeQueryParamConverter = new TaoBaoDailyNoticeQueryParamConverter(environment, clientWrapper);

    }



    public ItemGood tbQueryItemGood(String itemId) {
        TbkItemInfoGetRequest req = goodsDetailQueryParamConverter.toOpenReq(itemId);
        TbkItemInfoGetResponse res = clientWrapper.tbExecute(req);
        return goodsDetailQueryParamConverter.toMyRes(res, itemId);
    }

    public List<DailyNoticeItem> tbQueryDailyGoods(TbOpenApiQueryParam params) {
        TbkDgOptimusMaterialRequest req = dailyNoticeQueryParamConverter.toOpenReq(params);
        TbkDgOptimusMaterialResponse res = clientWrapper.tbExecute(req);
        return dailyNoticeQueryParamConverter.toMyRes(res, params);
    }

    public List<SendGood> tbQuerySendGoods(TbOpenApiQueryParam params) {
        TbkDgOptimusMaterialRequest req = sendGoodResultParamConverter.toOpenReq(params);
        TbkDgOptimusMaterialResponse res = clientWrapper.tbExecute(req);
        return sendGoodResultParamConverter.toMyRes(res, params);
    }


    public Page<ListGood> pddPageQueryGoods(PddOpenApiQueryParam param) {
        PddDdkGoodsSearchRequest req = pddListGoodParamConverter.toOpenReq(param);
        PddDdkGoodsSearchResponse rsp = clientWrapper.pddExecute(req);
        return pddListGoodParamConverter.toMyRes(rsp, param);
    }

    public Page<ListGood> tbQueryByMaterialId(TbOpenApiQueryParam param) {
        TbkDgOptimusMaterialRequest req = materialParamConverter.toOpenReq(param);
        TbkDgOptimusMaterialResponse res = clientWrapper.tbExecute(req);
        return new Page<>(materialParamConverter.toMyRes(res, param), param.getPageNo(), res.getTotalCount());
    }

    public Page<ListGood> tbQueryByGjz(TbOpenApiQueryParam param) {
        TbkDgMaterialOptionalRequest req = keywordQueryParamConverter.toOpenReq(param);
        TbkDgMaterialOptionalResponse res = clientWrapper.tbExecute(req);
        return new Page<>(keywordQueryParamConverter.toMyRes(res, param), param.getPageNo(), res.getTotalResults());
    }

}
