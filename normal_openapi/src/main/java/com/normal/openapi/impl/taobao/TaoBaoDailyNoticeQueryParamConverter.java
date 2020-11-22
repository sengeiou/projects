package com.normal.openapi.impl.taobao;

import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.openapi.TbOpenApiQueryParam;
import com.normal.model.shop.ListGood;
import com.normal.openapi.impl.ClientWrapper;
import com.normal.openapi.impl.ParamConverter;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class TaoBaoDailyNoticeQueryParamConverter implements ParamConverter<TbOpenApiQueryParam, TbkDgOptimusMaterialRequest, TbkDgOptimusMaterialResponse, List<DailyNoticeItem>> {

    private Environment environment;

    private ClientWrapper clientWrapper;

    public TaoBaoDailyNoticeQueryParamConverter(Environment environment, ClientWrapper clientWrapper) {
        this.environment = environment;
        this.clientWrapper = clientWrapper;
    }

    @Override
    public TbkDgOptimusMaterialRequest toOpenReq(TbOpenApiQueryParam myReqParam) {
        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("openapi.taobao.adzoneid")));
        req.setMaterialId(myReqParam.getTbMaterialId());
        req.setPageNo(Long.valueOf(String.valueOf(myReqParam.get("pageNo"))));
        req.setPageSize(10L);
        if(myReqParam.get("favoritesId") == null){
            req.setFavoritesId(environment.getProperty("autosend.favoritesId"));
        }else {
            req.setFavoritesId(String.valueOf(myReqParam.get("favoritesId")));
        }
        return req;
    }

    @Override
    public List<DailyNoticeItem> toMyRes(TbkDgOptimusMaterialResponse openBackParam ,TbOpenApiQueryParam myReqParam) {

        List<TbkDgOptimusMaterialResponse.MapData> rawGoods = openBackParam.getResultList();
        return rawGoods.stream()
                .map(mapData -> {
                    TaobaoConvertFunctions functions = new TaobaoConvertFunctions(mapData);
                    return functions.covertDailyNoticeItem(clientWrapper, environment);

                })
                .collect(Collectors.toList());

    }

    public List<ListGood> toListGoods(TbkDgOptimusMaterialResponse openBackParam) {

        List<TbkDgOptimusMaterialResponse.MapData> rawGoods = openBackParam.getResultList();
        return rawGoods.stream()
                .map(mapData -> {
                    TaobaoConvertFunctions functions = new TaobaoConvertFunctions(mapData);
                    return functions.convertListGood();

                })
                .collect(Collectors.toList());

    }

}
