package com.normal.openapi.impl.taobao;

import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.openapi.impl.ParamConverter;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class TaoBaoDailyNoticeQueryParamConverter implements ParamConverter<Map<String, Object>, TbkDgOptimusMaterialRequest, TbkDgOptimusMaterialResponse, List<DailyNoticeItem>> {

    private Environment environment;

    private TaobaoClientWrapper clientWrapper;

    public TaoBaoDailyNoticeQueryParamConverter(Environment environment, TaobaoClientWrapper clientWrapper) {
        this.environment = environment;
        this.clientWrapper = clientWrapper;
    }

    @Override
    public TbkDgOptimusMaterialRequest toOpenReq(Map<String, Object> myReqParam) {
        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("openapi.taobao.adzoneid")));
        if (myReqParam instanceof DefaultPageOpenApiQueryParam) {
            req.setMaterialId(Long.valueOf(((DefaultPageOpenApiQueryParam) myReqParam).getQueryType().key()));
        }
        req.setPageNo(Long.valueOf(String.valueOf(myReqParam.get("pageNo"))));
        req.setPageSize(10L);
        req.setFavoritesId(environment.getProperty("autosend.favoritesId"));
        return req;
    }

    @Override
    public List<DailyNoticeItem> toMyRes(TbkDgOptimusMaterialResponse openBackParam) {

        List<TbkDgOptimusMaterialResponse.MapData> rawGoods = openBackParam.getResultList();
        return rawGoods.stream()
                .map(mapData -> {
                    TaobaoConvertFunctions functions = new TaobaoConvertFunctions(mapData);
                    return functions.covertDailyNoticeItem(clientWrapper, environment);

                })
                .collect(Collectors.toList());

    }
}
