package com.normal.model.openapi;

public enum TaobaoMaterialIds {

    /**
     * 聚划算满减
     */
    OTHER_JHSMJ("32366"),

    /**
     * 天猫满减
     */
    OTHER_TMCSMJ("27160"),

    /**
     * 相似推荐
     */
    OTHER_XSTJ("13256"),
    /**
     * 好券直播-综合
     */
    HQZB_ZH("3756"),
    /**
     * 好券直播-	鞋包配饰
     */
    HQZB_XBPS("3762"),
    /**
     * 好券直播-	母婴
     */
    HQZB_MY("3760"),
    /**
     * 好券直播-女装
     */
    HQZB_NVZ("3767"),
    /**
     * 好券直播-美妆个护
     */
    HQZB_MZGH("3763"),
    /**
     * 好券直播-	食品
     */
    HQZB_SP("3761"),
    /**
     * 好券直播-家居家装
     */
    HQZB_JJJZ("3758"),
    /**
     * 好券直播-	男装
     */
    HQZB_NANZ("3764"),
    /**
     * 好券直播-运动户外
     */
    HQZB_YDHW("3766"),
    /**
     * 好券直播-数码家电
     */
    HQZB_SMJD("3759"),
    /**
     * 好券直播-	内衣
     */
    HQZB_NY("3765"),


    /**
     * 大额券 -	综合
     */
    DEQ_ZH("27446"),
    /**
     * 大额券 -	女装
     */
    DEQ_NZ("27448"),
    /**
     * 大额券 -	食品
     */
    DEQ_SP("27451"),
    /**
     * 大额券 -	美妆个护
     */
    DEQ_MZGH("27453"),
    /**
     * 大额券 -	家居家装
     */
    DEQ_JJJZ("27798"),
    /**
     * 大额券 -	母婴
     */
    DEQ_MY("27454"),

    /**
     * 高佣榜 -	综合
     */
    GYQ_ZH("13366"),
    /**
     * 高佣榜 -	鞋包配饰
     */
    GYQ_XBPS("13370"),
    /**
     * 高佣榜 -	母婴
     */
    GYQ_MY("13374"),
    /**
     * 高佣榜 -	女装
     */
    GYQ_NVZ("13367"),
    /**
     * 高佣榜 -	 美妆个护
     */
    GYQ_MZGH("13371"),
    /**
     * 高佣榜 -	食品
     */
    GYQ_SP("13375"),
    /**
     * 高佣榜 -	家居家装
     */
    GYQ_JJJZ("13368"),
    /**
     * 高佣榜 -	男装
     */
    GYQ_NANZ("13372"),
    /**
     * 高佣榜 -	 运动户外
     */
    GYQ_YDHW("13376"),
    /**
     * 高佣榜 -	数码家电
     */
    GYQ_SMJD("13369"),
    /**
     * 高佣榜 -	内衣
     */
    GYQ_NY("13373"),

    /**
     * 品牌券 -	综合
     */
    PPQ_ZH("3786"),

    /**
     * 品牌券 -	 鞋包配饰
     */
    PPQ_XBPS("3796"),

    /**
     * 品牌券 -	母婴
     */
    PPQ_MY("3789"),

    /**
     * 品牌券 -	女装
     */
    PPQ_NZ("3788"),

    /**
     * 品牌券 -	 美妆个护
     */
    PPQ_MZGH("3794"),

    /**
     * 品牌券 -	食品
     */
    PPQ_SP("3791"),
    /**
     * 品牌券 -	家居家装
     */
    PPQ_JJJZ("3792"),
    /**
     * 品牌券 -	男装
     */
    PPQ_NANZ("3790"),
    /**
     * 品牌券 -	 运动户外
     */
    PPQ_YDHW("3795"),
    /**
     * 品牌券 -	数码家电
     */
    PPQ_SMJD("3793"),
    /**
     * 品牌券 -	内衣
     */
    PPQ_NY("3787"),
    /**
     * 爆款特卖 - 猫超1元购凑单API
     */
    BKTM_1YG("27162"),
    /**
     * 爆款特卖 - 猫超第二件0元API
     */
    BKTM_2J0("27161"),
    /**
     * 爆款特卖 - 猫超单件满减包邮API
     */
    BKTM_DJBY("27160"),
    /**
     * 爆款特卖 - 聚划算热卖
     */
    BKTM_JHSRM("31371"),
    /**
     * 爆款特卖 - 天天特卖
     */
    BKTM_TTTM("31362"),
    /**
     * 爆款特卖 - 特惠
     */
    BKTM_TH("4094");

    private String value;

    public String getValue() {
        return value;
    }

    TaobaoMaterialIds(java.lang.String value) {
        this.value = value;
    }

    public static String typeOf(String materialId) {
        for (TaobaoMaterialIds constant : TaobaoMaterialIds.class.getEnumConstants()) {
            String name = constant.name();
            if (materialId.equals(constant.getValue())) {
                return name.substring(0, name.indexOf("_"));
            }
        }
        return null;
    }

}
