package com.normal.openapi.impl.taobao;

import com.normal.model.BizDictEnums;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.ListGood;
import com.normal.openapi.impl.ParamConverter;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class TaoBaoKeywordQueryParamConverter implements ParamConverter<DefaultPageOpenApiQueryParam, TbkDgMaterialOptionalRequest, TbkDgMaterialOptionalResponse, List<ListGood>> {

    private Environment environment;

    public TaoBaoKeywordQueryParamConverter(Environment environment) {
        this.environment = environment;

    }

    @Override
    public TbkDgMaterialOptionalRequest toOpenReq(DefaultPageOpenApiQueryParam myReqParam) {
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("openapi.taobao.adzoneid")));
        Object keyword = myReqParam.get("keyword");
        if (StringUtils.isEmpty(keyword)) {
            throw new IllegalArgumentException("查询关键字不可为空");
        }
        req.setQ((String) keyword);
        //无线平台
        req.setPlatform(2L);
        //排序
        req.setSort(myReqParam.getTaobaoSort());
        //个性化推荐
        req.setMaterialId(Long.valueOf(BizDictEnums.OTHER_GXHTJ.key()));
        req.setPageNo(Long.valueOf(String.valueOf(myReqParam.get("pageNo"))));
        req.setPageSize(Long.valueOf(String.valueOf(myReqParam.get("pageSize"))));
        return req;
    }

    @Override
    public List<ListGood> toMyRes(TbkDgMaterialOptionalResponse openBackParam) {
        List<TbkDgMaterialOptionalResponse.MapData> rawGoods = openBackParam.getResultList();
        List<ListGood> list = rawGoods.stream()
                .map((item) -> new TaobaoConvertFunctions(item).convertListGood())
                .collect(Collectors.toList());
        return list;
    }
}
