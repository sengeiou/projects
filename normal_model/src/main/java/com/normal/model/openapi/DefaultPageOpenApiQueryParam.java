package com.normal.model.openapi;

import com.normal.model.BizDictEnums;
import com.normal.model.PageParam;

/**
 * @author: fei.he
 */
public class DefaultPageOpenApiQueryParam extends PageParam {

    public final static String platform = "platform";
    public final static String catId = "catId";
    public final static String keyword = "keyword";


    public final static String tbOrderBy = "tbOrderBy";
    public final static String tbAsc = "tbAsc";
    public final static String tbMaterialId = "tbMaterialId";

    /**
     * 排序方式:0-综合排序;1-按佣金比率升序;2-按佣金比例降序;3-按价格升序;4-按价格降序;5-按销量升序;6-按销量降序;7-优惠券金额排序升序;8-优惠券金额排序降序;9-券后价升序排序;10-券后价降序排序;11-按照加入多多进宝时间升序;12-按照加入多多进宝时间降序;13-按佣金金额升序排序;14-按佣金金额降序排序;15-店铺描述评分升序;16-店铺描述评分降序;17-店铺物流评分升序;18-店铺物流评分降序;19-店铺服务评分升序;20-店铺服务评分降序;27-描述评分击败同类店铺百分比升序，28-描述评分击败同类店铺百分比降序，29-物流评分击败同类店铺百分比升序，30-物流评分击败同类店铺百分比降序，31-服务评分击败同类店铺百分比升序，32-服务评分击败同类店铺百分比降序
     */
    public final static String pddSort = "pddSort";


    private DefaultPageOpenApiQueryParam() {
        super();
        put(tbOrderBy, BizDictEnums.DEFAULT_ORDER_BY);
        put(tbAsc, BizDictEnums.COMMON_DES);
        put("pageSize", 5);
    }

    public static DefaultPageOpenApiQueryParam newInstance() {
        return new DefaultPageOpenApiQueryParam();
    }


    public DefaultPageOpenApiQueryParam setTbOrderBy(BizDictEnums orderByType) {
        if (orderByType.getType() != BizDictEnums.DEFAULT_ORDER_BY.getType()) {
            throw new IllegalArgumentException("不支持排序类型:" + orderByType);
        }
        return setParam(tbOrderBy, orderByType);
    }

    public DefaultPageOpenApiQueryParam setTbOrderDirect(BizDictEnums orderDirect) {
        if (orderDirect != BizDictEnums.COMMON_ASC && orderDirect != BizDictEnums.COMMON_DES) {
            throw new IllegalArgumentException("不支持排序方向类型:" + orderDirect);
        }
        return setParam(tbAsc, orderDirect);
    }

    public DefaultPageOpenApiQueryParam setTbMaterialId(String materialId) {
        return setParam(tbMaterialId, materialId);
    }


    public DefaultPageOpenApiQueryParam setParam(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public String getTbSort() {
        return getTbOrderBy().key() + getTbAsc().key();
    }

    public BizDictEnums getTbOrderBy() {
        return getValue(tbOrderBy, BizDictEnums.class);
    }

    public BizDictEnums getTbAsc() {
        return getValue(tbOrderBy, BizDictEnums.class);
    }

    public String getTbMaterialId() {
        return getValue(tbMaterialId, String.class);
    }

    public String getPlatform() {
        return getValue(platform, String.class);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String value = (String) get(key);
        if (clazz.equals(Long.class)) {
            return (T) Long.valueOf(value);
        }
        if (clazz.equals(Integer.class)) {
            return (T) Integer.valueOf(value);
        }
        return (T) value;
    }

    public void checkNull(String property) {
        if (get(property) == null) {
            throw new IllegalArgumentException("property: " + property + "is not allowed  null");
        }
    }

}
