package com.normal.model.openapi;

import com.normal.model.BizDictEnums;

/**
 * @author: fei.he
 */
public class TbOpenApiQueryParam extends DefaultPageOpenApiQueryParam {

    public final static String tbOrderBy = "tbOrderBy";
    public final static String tbAsc = "tbAsc";
    public final static String tbMaterialId = "tbMaterialId";
    public final static String tbQueryType = "tbQueryType";

    public TbOpenApiQueryParam() {
        super();
        put(tbOrderBy, BizDictEnums.DEFAULT_ORDER_BY.key());
        put(tbAsc, BizDictEnums.COMMON_DES.key());
    }

    public TbOpenApiQueryParam(DefaultPageOpenApiQueryParam defaultParam) {
        super();
        put(tbOrderBy, BizDictEnums.DEFAULT_ORDER_BY.key());
        put(tbAsc, BizDictEnums.COMMON_DES.key());
        this.putAll(defaultParam);
    }

    public TbOpenApiQueryParam setTbOrderBy(BizDictEnums orderByType) {
        if (orderByType.getType() != BizDictEnums.DEFAULT_ORDER_BY.getType()) {
            throw new IllegalArgumentException("不支持排序类型:" + orderByType);
        }
        return (TbOpenApiQueryParam) setParam(tbOrderBy, orderByType);
    }


    public TbOpenApiQueryParam setTbOrderDirect(BizDictEnums orderDirect) {
        if (orderDirect != BizDictEnums.COMMON_ASC && orderDirect != BizDictEnums.COMMON_DES) {
            throw new IllegalArgumentException("不支持排序方向类型:" + orderDirect);
        }
        return (TbOpenApiQueryParam) setParam(tbAsc, orderDirect);
    }

    public TbOpenApiQueryParam setTbMaterialId(Long materialId) {
        return (TbOpenApiQueryParam) setParam(tbMaterialId, materialId);
    }

    public String getTbOrderBy() {
        return getValue(tbOrderBy, String.class);
    }

    public String getTbAsc() {
        return getValue(tbAsc, String.class);
    }

    public Long getTbMaterialId() {
        return getValue(tbMaterialId, Long.class);
    }


    public String getTbSort() {
        return getTbOrderBy() + getTbAsc();
    }

}
