package com.normal.model.openapi;

import com.normal.model.BizDictEnums;
import com.normal.model.PageParam;

/**
 * @author: fei.he
 */
public class DefaultPageOpenApiQueryParam extends PageParam {

    private final static String queryTypeKey = "queryTypeKey";
    private final static String orderByKey = "orderByKey";
    private final static String ascKey = "asc";

    private DefaultPageOpenApiQueryParam() {
        super();
        put(queryTypeKey, BizDictEnums.DEFAULT_QUERY_TYPE);
        put(orderByKey, BizDictEnums.DEFAULT_ORDER_BY);
        put(ascKey, BizDictEnums.COMMON_YES);
    }

    public static DefaultPageOpenApiQueryParam newInstance() {
        return new DefaultPageOpenApiQueryParam();
    }

    public DefaultPageOpenApiQueryParam withQueryType(BizDictEnums queryType) {
        if (queryType.getType() != BizDictEnums.DEFAULT_QUERY_TYPE.getType()) {
            throw new IllegalArgumentException("不支持查询类型:" + queryType);
        }
        this.put(queryTypeKey, queryType);
        return this;
    }

    public DefaultPageOpenApiQueryParam withOrderBy(BizDictEnums orderByType) {
        if (orderByType.getType() != BizDictEnums.DEFAULT_ORDER_BY.getType()) {
            throw new IllegalArgumentException("不支持排序类型:" + orderByType);
        }
        this.put(orderByKey, orderByType);
        return this;
    }

    public DefaultPageOpenApiQueryParam withAsc(boolean asc) {
        this.put(ascKey, asc ? "_asc" : "_des");
        return this;
    }

    public DefaultPageOpenApiQueryParam withParam(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public BizDictEnums getQueryType() {
        return (BizDictEnums) get(queryTypeKey);
    }

    public BizDictEnums getOrderBy() {
        return (BizDictEnums) get(orderByKey);
    }

    public String getAsc() {
        return (String) get(orderByKey);
    }

    public String getTaobaoSort() {
        return getOrderBy().key() + getAsc();
    }
}
