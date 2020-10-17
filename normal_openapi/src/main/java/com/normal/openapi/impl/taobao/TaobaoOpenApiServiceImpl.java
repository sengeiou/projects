package com.normal.openapi.impl.taobao;

import com.normal.base.mybatis.Page;
import com.normal.model.BizDictEnums;
import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.autosend.SendGood;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.ItemGood;
import com.normal.model.shop.ListGood;
import com.normal.openapi.IAutoSendGoodsQueryService;
import com.normal.openapi.IOpenApiService;
import com.normal.openapi.impl.ClientWrapper;
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
import java.util.Objects;

/**
 * @author fei.he
 */
@Component
@Qualifier("taobaoOpenApiService")
public class TaobaoOpenApiServiceImpl implements IOpenApiService, IAutoSendGoodsQueryService {

    @Autowired
    ClientWrapper clientWrapper;

    @Autowired
    Environment environment;

    private TaoBaoMaterialQueryParamConverter materialParamConverter;
    private TaoBaoKeywordQueryParamConverter keywordQueryParamConverter;
    private TaoBaoGoodsDetailQueryParamConverter goodsDetailQueryParamConverter;
    private TaoBaoSendGoodResultParamConverter sendGoodResultParamConverter;
    private TaoBaoDailyNoticeQueryParamConverter dailyNoticeQueryParamConverter;

    @PostConstruct
    public void init() {
        materialParamConverter = new TaoBaoMaterialQueryParamConverter(environment, clientWrapper);
        keywordQueryParamConverter = new TaoBaoKeywordQueryParamConverter(environment);
        goodsDetailQueryParamConverter = new TaoBaoGoodsDetailQueryParamConverter();
        sendGoodResultParamConverter = new TaoBaoSendGoodResultParamConverter(environment, clientWrapper, materialParamConverter);
        dailyNoticeQueryParamConverter = new TaoBaoDailyNoticeQueryParamConverter(environment, clientWrapper);

    }

    @Override
    public List<SendGood> querySendGoods(Map<String, Object> params) {
        TbkDgOptimusMaterialRequest req = sendGoodResultParamConverter.toOpenReq(params);
        TbkDgOptimusMaterialResponse res = clientWrapper.tbExecute(req);
        return sendGoodResultParamConverter.toMyRes(res, params);
    }


    @Override
    public Page<ListGood> pageQueryGoods(DefaultPageOpenApiQueryParam param) {
        String queryType = param.getTbMaterialId();
        if (BizDictEnums.QUERY_GJZ.key().equals(queryType)) {
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
        if (param.getTbOrderBy() == null) {
            param.setTbOrderBy(BizDictEnums.DEFAULT_ORDER_BY)
                    .setTbOrderDirect(BizDictEnums.COMMON_DES);
        }
        TbkDgMaterialOptionalRequest req = keywordQueryParamConverter.toOpenReq(param);
        TbkDgMaterialOptionalResponse res = clientWrapper.tbExecute(req);
        return new Page<>(keywordQueryParamConverter.toMyRes(res, param), param.getPageNo(), res.getTotalResults());
    }

    /**
     * 根据物料id查询
     *
     * @param param
     * @return
     */
    private Page<ListGood> queryByMaterialId(DefaultPageOpenApiQueryParam param) {
        Objects.requireNonNull(param.getTbMaterialId());
        TbkDgOptimusMaterialRequest req = materialParamConverter.toOpenReq(param);
        TbkDgOptimusMaterialResponse res = clientWrapper.tbExecute(req);
        return new Page<>(materialParamConverter.toMyRes(res, param), param.getPageNo(), res.getTotalCount());
    }

    @Override
    public ItemGood queryItemGood(String itemId) {
        TbkItemInfoGetRequest req = goodsDetailQueryParamConverter.toOpenReq(itemId);
        TbkItemInfoGetResponse res = clientWrapper.tbExecute(req);
        return goodsDetailQueryParamConverter.toMyRes(res, itemId);
    }

    @Override
    public List<DailyNoticeItem> queryDailyGoods(Map<String, Object> params) {
        TbkDgOptimusMaterialRequest req = dailyNoticeQueryParamConverter.toOpenReq(params);
        TbkDgOptimusMaterialResponse res = clientWrapper.tbExecute(req);
        return dailyNoticeQueryParamConverter.toMyRes(res, params);
    }


}