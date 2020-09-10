package com.normal.openapi.impl;

import com.normal.base.utils.Dates;
import com.normal.model.BizDictEnums;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.springframework.util.StringUtils;

import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * @author: fei.he
 */
public class GoodsTextGeneratorFactory {

    public static IGoodsTextGenerator getTextGenerator(String materialId, TbkDgOptimusMaterialResponse.MapData item, Supplier<String> pwdSupplier) {
        //大额券
        if (BizDictEnums.DEQ_ZH.key().equals(materialId)) {
            return new Generator4DEQ(item, pwdSupplier);
        } else {
            return new BaseGenerator(item, pwdSupplier);
        }
    }

    private static abstract class AbstractTextGenerator implements IGoodsTextGenerator {

        TbkDgOptimusMaterialResponse.MapData item;

        StringJoiner joiner = new StringJoiner("\n");

        Supplier<String> pwdSupplier;

        public AbstractTextGenerator(TbkDgOptimusMaterialResponse.MapData item, Supplier<String> pwdSupplier) {
            this.item = item;
            this.pwdSupplier = pwdSupplier;
        }

        protected StringJoiner join(StringJoiner joiner, String subStr) {
            if (!StringUtils.isEmpty(subStr)) {
                joiner.add(subStr);
            }
            return joiner;
        }

        @Override
        public String text() {
            buildText();
            buildLastRow();
            return joiner.toString();
        }

        private void buildLastRow() {
            joiner.add("复制这段话,打开taobao即可领券购买");
        }

        protected abstract void buildText();
    }

    /**
     * 大额券
     */
    private static class Generator4DEQ extends AbstractTextGenerator {

        public Generator4DEQ(TbkDgOptimusMaterialResponse.MapData item, Supplier<String> pwdSupplier) {
            super(item, pwdSupplier);
        }

        @Override
        protected void buildText() {
            join(joiner, "【商品名称】" + item.getTitle());
            join(joiner, item.getSubTitle());
            join(joiner, "【原价】" + item.getCouponStartFee());
            join(joiner, "【优惠减免】" + item.getCouponAmount());
            join(joiner, "【优惠券信息】" + item.getCouponInfo());
            join(joiner, "【券有效截止日期】" + Dates.format(Long.valueOf(item.getCouponEndTime())));
            join(joiner, "【下单口令】" + this.pwdSupplier.get());
        }
    }

    private static class BaseGenerator extends AbstractTextGenerator {

        public BaseGenerator(TbkDgOptimusMaterialResponse.MapData item, Supplier<String> pwdSupplier) {
            super(item, pwdSupplier);
        }

        @Override
        protected void buildText() {
            join(joiner, "【商品名称】" + item.getTitle());
            join(joiner, item.getSubTitle());
            join(joiner, "【原价】" + item.getReservePrice());
            join(joiner, "【优惠减免】" + item.getCouponAmount());
            join(joiner, "【券有效截止日期】" + Dates.format(Long.valueOf(item.getCouponEndTime())));
            join(joiner, "【下单口令】" + this.pwdSupplier.get());
        }
    }
}
