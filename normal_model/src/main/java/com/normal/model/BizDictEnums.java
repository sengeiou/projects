package com.normal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
public enum BizDictEnums implements NormalEnum {

    OFFER_TYPE_MJ(1, "0", "满减"),
    OFFER_TYPE_YHQ(1, "1", "优惠券"),

    COMMON_YES(2, "0", "是"),
    COMMON_NO(2, "1", "否"),
    COMMON_ASC(2, "_asc", "升序"),
    COMMON_DES(2, "_des", "降序"),



    OTHER_XSTJ(3, "13256", "相似推荐"),
    OTHER_GXHTJ(3, "17004", "个性化推荐"),
    OTHER_XPK(3, "31519", "选品库"),
    OTHER_XPKSP(3, "31539", "选品库商品"),

    
    HQZB_ZH(4, "3756", "好券直播"),
    HQZB_XBPS(4, "3762", "鞋包配饰"),
    HQZB_MY(4, "3760", "母婴"),
    HQZB_NVZ(4, "3767", "女装"),
    HQZB_MZGH(4, "3763", "美妆个护"),
    HQZB_SP(4, "3761", "食品"),
    HQZB_JJJZ(4, "3758", "家居家装"),
    HQZB_NANZ(4, "3764", "男装"),
    HQZB_YDHW(4, "3766", "运动户外"),
    HQZB_SMJD(4, "3759", "数码家电"),
    HQZB_NY(4, "3765", "内衣"),


    DEQ_ZH(5, "27446", "大额券"),
    DEQ_NZ(5, "27448", "女装"),
    DEQ_SP(5, "27451", "食品"),
    DEQ_MZGH(5, "27453", "美妆个护"),
    DEQ_JJJZ(5, "27798", "家居家装"),
    DEQ_MY(5, "27454", "母婴"),

    
    GYQ_ZH(6, "13366", "推荐"),
    GYQ_XBPS(6, "13370", "鞋包配饰"),
    GYQ_MY(6, "13374", "母婴"),
    GYQ_NVZ(6, "13367", "女装"),
    GYQ_MZGH(6, "13371", "美妆个护"),
    GYQ_SP(6, "13375", "食品"),
    GYQ_JJJZ(6, "13368", "家居家装"),
    GYQ_NANZ(6, "13372", "男装"),
    GYQ_YDHW(6, "13376", "运动户外"),
    GYQ_SMJD(6, "13369", "数码家电"),
    GYQ_NY(6, "13373", "内衣"),

    
    PPQ_ZH(7, "3786", "品牌"),
    PPQ_XBPS(7, "3796", "鞋包配饰"),
    PPQ_MY(7, "3789", "母婴"),
    PPQ_NZ(7, "3788", "女装"),
    PPQ_MZGH(7, "3794", "美妆个护"),
    PPQ_SP(7, "3791", "食品"),
    PPQ_JJJZ(7, "3792", "家居家装"),
    PPQ_NANZ(7, "3790", "男装"),
    PPQ_YDHW(7, "3795", "运动户外"),
    PPQ_SMJD(7, "3793", "数码家电"),
    PPQ_NY(7, "3787", "内衣"),

    //实时热销 - 综合
    RM_ZH(8, "28026", "精选"),
    RM_TTTM(8, "31362", "天天特卖"),
    RM_TH(8, "4094", "特惠"),
    RM_1YG(8, "27162", "猫超1元购凑单"),
    RM_2J0(8, "27161", "猫超第二件0元"),
    RM_DJBY(8, "27160", "猫超单件满减包邮"),
    RM_JHSRM(8, "31371", "聚划算热卖"),
    RM_JHSMJ(3, "32366", "聚划算满减"),
    RM_FS(8, "28029", "热销服饰"),
    RM_KX(8, "28027", "热销快消"),
    RM_DQMJ(8, "28028", "热销电器美家"),

    /**
     * 商品查询类型
     */
    DEFAULT_QUERY_TYPE(9, "13366", "高佣综合查询"),
    QUERY_SSRX(9, "28026", "实时热销"),
    QUERY_XGTJ(9, "13256", "相关推荐查询"),
    QUERY_CNXH(9, "6708", "猜你喜欢"),
    QUERY_GJZ(9, "4", "根据关键字查询"),

    /**
     * 排序类型
     */
    DEFAULT_ORDER_BY(10, "tk_rate", "淘客佣金比例"),
    ORDER_BY_SELLNUM(10, "total_sales", "销量"),
    ORDER_BY_JIAGE(10, "price", "价格"),

    /**
     * banner 类型
     */
    BANNER_TYPE_PAGE(11, "banner_page", "引用第三方的banner"),

    /**
     * 平台类型
     */
    PLATFORM_TB(12, "tb", "tb"),
    PLATFORM_PDD(12, "pdd", "pdd"),


    /**
     * 配置活动类型
     */
    ACTIVITY_TYPE_COLL(13, "coll", "coll"),
    ACTIVITY_TYPE_BANNER(13, "banner", "banner"),
    ;


    private int type;
    private String key;
    private String value;

    BizDictEnums(int type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String value() {
        return value;
    }

    final static Map<Integer, List<BizDictEnums>> typeMapCache = new HashMap<>(8);
    final static Map<String, BizDictEnums> keyMapCache = new HashMap<>(8);

    static {
        for (BizDictEnums item : BizDictEnums.values()) {
            keyMapCache.put(item.key, item);
            List<BizDictEnums> enums = typeMapCache.get(item.type);
            if (enums == null) {
                enums = new ArrayList<>();
                typeMapCache.put(item.type, enums);
            }
            enums.add(item);
        }
    }

    public static List<BizDictEnums> getValuesByType(int type) {
        return typeMapCache.get(type);
    }

    public static BizDictEnums ofKey(String key) {
        return keyMapCache.get(key);
    }


}


