package com.normal.openapi.impl;

import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.ListGood;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class MaterialQueryParamConverter implements ParamConverter<Map<String, Object>, TbkDgOptimusMaterialRequest, TbkDgOptimusMaterialResponse, List<ListGood>> {

    private Environment environment;

    public MaterialQueryParamConverter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public TbkDgOptimusMaterialRequest toOpenReq(Map<String, Object> myReqParam) {
        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("openapi.taobao.adzoneid")));
        Object materialId = myReqParam.get("materialId");
        if (myReqParam instanceof DefaultPageOpenApiQueryParam) {
            req.setMaterialId(Long.valueOf(((DefaultPageOpenApiQueryParam) myReqParam).getQueryType().key()));
        }else {
            if (Objects.nonNull(materialId)  && !StringUtils.isEmpty(materialId)) {
                req.setMaterialId(Long.valueOf(String.valueOf(materialId)));
            }
        }

        req.setPageNo(Long.valueOf(String.valueOf(myReqParam.get("pageNo"))));
        req.setPageSize(Long.valueOf(String.valueOf(myReqParam.get("pageSize"))));
        return req;
    }

    @Override
    public List<ListGood> toMyRes(TbkDgOptimusMaterialResponse openBackParam) {
        List<TbkDgOptimusMaterialResponse.MapData> rawGoods = openBackParam.getResultList();
        List<ListGood> list = rawGoods.stream()
                .map((item) -> new TaobaoListGoodConvertFunction(item).convert())
                .collect(Collectors.toList());
        return list;
    }
}
