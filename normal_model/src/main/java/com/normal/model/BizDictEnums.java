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

    OTHER_JHSMJ(3, "32366", "聚划算满减"),
    OTHER_TMCSMJ(3, "27160", "天猫满减"),
    OTHER_XSTJ(3, "13256", "相似推荐"),
    OTHER_CXHTJ(3, "17004", "个性化推荐"),

    HQZB_ZH(4, "3756", "好券直播-综合"),
    HQZB_XBPS(4, "3762", "好券直播-鞋包配饰"),
    HQZB_MY(4, "3760", "好券直播-母婴"),
    HQZB_NVZ(4, "3767", "好券直播-女装"),
    HQZB_MZGH(4, "3763", "好券直播-美妆个护"),
    HQZB_SP(4, "3761", "好券直播-食品"),
    HQZB_JJJZ(4, "3758", "好券直播-家居家装"),
    HQZB_NANZ(4, "3764", "好券直播-男装"),
    HQZB_YDHW(4, "3766", "好券直播-运动户外"),
    HQZB_SMJD(4, "3759", "好券直播-数码家电"),
    HQZB_NY(4, "3765", "好券直播-内衣"),


    DEQ_ZH(5, "27446", "大额券-综合"),
    DEQ_NZ(5, "27448", "大额券-女装"),
    DEQ_SP(5, "27451", "大额券-食品"),
    DEQ_MZGH(5, "27453", "大额券-美妆个护"),
    DEQ_JJJZ(5, "27798", "大额券-家居家装"),
    DEQ_MY(5, "27454", "大额券-母婴"),

    GYQ_ZH(6, "13366", "高佣榜-综合"),
    GYQ_XBPS(6, "13370", "高佣榜-鞋包配饰"),
    GYQ_MY(6, "13374", "高佣榜-母婴"),
    GYQ_NVZ(6, "13367", "高佣榜-女装"),
    GYQ_MZGH(6, "13371", "高佣榜-美妆个护"),
    GYQ_SP(6, "13375", "高佣榜-食品"),
    GYQ_JJJZ(6, "13368", "高佣榜-家居家装"),
    GYQ_NANZ(6, "13372", "高佣榜-男装"),
    GYQ_YDHW(6, "13376", "高佣榜-运动户外"),
    GYQ_SMJD(6, "13369", "高佣榜-数码家电"),
    GYQ_NY(6, "13373", "高佣榜-内衣"),

    PPQ_ZH(7, "3786", "品牌券-综合"),
    PPQ_XBPS(7, "3796", "品牌券-鞋包配饰"),
    PPQ_MY(7, "3789", "品牌券-母婴"),
    PPQ_NZ(7, "3788", "品牌券-女装"),
    PPQ_MZGH(7, "3794", "品牌券-美妆个护"),
    PPQ_SP(7, "3791", "品牌券-食品"),
    PPQ_JJJZ(7, "3792", "品牌券-家居家装"),
    PPQ_NANZ(7, "3790", "品牌券-男装"),
    PPQ_YDHW(7, "3795", "品牌券-运动户外"),
    PPQ_SMJD(7, "3793", "品牌券-数码家电"),
    PPQ_NY(7, "3787", "品牌券-内衣"),

    BKTM_1YG(8, "27162", "爆款特卖-猫超1元购凑单API"),
    BKTM_2J0(8, "27161", "爆款特卖-猫超第二件0元API"),
    BKTM_DJBY(8, "27160", "爆款特卖-猫超单件满减包邮API"),
    BKTM_JHSRM(8, "31371", "爆款特卖-聚划算热卖"),
    BKTM_TTTM(8, "31362", "爆款特卖-天天特卖"),
    BKTM_TH(8, "4094", "爆款特卖-特惠"),


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
    public int key() {
        return Integer.valueOf(key);
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


