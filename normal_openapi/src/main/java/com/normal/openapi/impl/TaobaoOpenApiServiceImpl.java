package com.normal.openapi.impl;

import com.normal.base.mybatis.Page;
import com.normal.model.BizDictEnums;
import com.normal.model.autosend.SendGood;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.ItemGood;
import com.normal.model.shop.ListGood;
import com.normal.openapi.IOpenApiService;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author fei.he
 */
@Component
@Qualifier("taobaoOpenApiService")
public class TaobaoOpenApiServiceImpl implements IOpenApiService {

    @Autowired
    TaobaoClientWrapper clientWrapper;

    @Autowired
    Environment environment;

    private MaterialQueryParamConverter materialParamConverter;
    private KeywordQueryParamConverter keywordQueryParamConverter;
    private GoodsDetailQueryParamConverter goodsDetailQueryParamConverter;
    private SendGoodResultParamConverter sendGoodResultParamConverter;

    @PostConstruct
    public void init() {
        materialParamConverter = new MaterialQueryParamConverter(environment);
        keywordQueryParamConverter = new KeywordQueryParamConverter(environment);
        goodsDetailQueryParamConverter = new GoodsDetailQueryParamConverter();
        sendGoodResultParamConverter = new SendGoodResultParamConverter(environment, clientWrapper, materialParamConverter);
    }

    @Override
    public List<SendGood> querySendGoods(Map<String, Object> params) {
        TbkDgOptimusMaterialRequest req = sendGoodResultParamConverter.toOpenReq(params);
        TbkDgOptimusMaterialResponse res = clientWrapper.execute(req);
        return sendGoodResultParamConverter.toMyRes(res);
    }


    @Override
    public Page<ListGood> pageQueryGoods(DefaultPageOpenApiQueryParam param) {
        BizDictEnums queryType = param.getQueryType();
        if (BizDictEnums.QUERY_GJZ.equals(queryType) || BizDictEnums.QUERY_GXHTJ.equals(queryType)) {
            return queryByGjz(param);
        } else {
            return queryByMaterialId(param);
        }
    }

    /**
     * 根据关键字查询
     *
     * @param param
     * @return
     */
    private Page<ListGood> queryByGjz(DefaultPageOpenApiQueryParam param) {
        if (param.getOrderBy() == null) {
            param.withOrderBy(BizDictEnums.DEFAULT_ORDER_BY)
                    .withAsc(false);
        }
        TbkDgMaterialOptionalRequest req = keywordQueryParamConverter.toOpenReq(param);
        TbkDgMaterialOptionalResponse res = clientWrapper.execute(req);
        return new Page<>(keywordQueryParamConverter.toMyRes(res), param.getPageNo(), res.getTotalResults());
    }

    /**
     * 根据物料id查询
     *
     * @param param
     * @return
     */
    private Page<ListGood> queryByMaterialId(DefaultPageOpenApiQueryParam param) {
        if (param.getQueryType() == null) {
            param.withQueryType(BizDictEnums.DEFAULT_QUERY_TYPE)
                    .withAsc(false);
        }
        TbkDgOptimusMaterialRequest req = materialParamConverter.toOpenReq(param);
        TbkDgOptimusMaterialResponse res = clientWrapper.execute(req);
        return new Page<>(materialParamConverter.toMyRes(res), param.getPageNo(), res.getTotalCount());
    }

    @Override
    public ItemGood queryItemGood(String itemId) {
        TbkItemInfoGetRequest req = goodsDetailQueryParamConverter.toOpenReq(itemId);
        TbkItemInfoGetResponse res = clientWrapper.execute(req);
        return goodsDetailQueryParamConverter.toMyRes(res);
    }


}