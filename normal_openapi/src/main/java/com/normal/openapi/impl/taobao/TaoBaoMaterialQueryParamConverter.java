package com.normal.openapi.impl.taobao;

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
public class TaoBaoMaterialQueryParamConverter implements ParamConverter<TbOpenApiQueryParam, TbkDgOptimusMaterialRequest, TbkDgOptimusMaterialResponse, List<ListGood>> {

    private Environment environment;
    private ClientWrapper clientWrapper;

    public TaoBaoMaterialQueryParamConverter(Environment environment, ClientWrapper clientWrapper) {
        this.environment = environment;
        this.clientWrapper = clientWrapper;
    }

    @Override
    public TbkDgOptimusMaterialRequest toOpenReq(TbOpenApiQueryParam myReqParam) {
        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("openapi.taobao.adzoneid")));
         req.setMaterialId(myReqParam.getTbMaterialId());
        req.setPageNo(Long.valueOf(String.valueOf(myReqParam.get("pageNo"))));
        req.setPageSize(Long.valueOf(String.valueOf(myReqParam.get("pageSize"))));
        return req;
    }

    @Override
    public List<ListGood> toMyRes(TbkDgOptimusMaterialResponse openBackParam, TbOpenApiQueryParam myReqParam) {
        List<TbkDgOptimusMaterialResponse.MapData> rawGoods = openBackParam.getResultList();
        List<ListGood> list = rawGoods.stream()
                .map(mapData -> new TaobaoConvertFunctions(mapData).convertListGood())
                .collect(Collectors.toList());
        return list;
    }
}
