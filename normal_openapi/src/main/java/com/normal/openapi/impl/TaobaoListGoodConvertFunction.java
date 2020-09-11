package com.normal.openapi.impl;

import com.normal.base.utils.Dates;
import com.normal.model.shop.CouponInfo;
import com.normal.model.shop.ListGood;
import com.normal.model.shop.OfferInfo;
import com.taobao.api.TaobaoObject;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class TaobaoListGoodConvertFunction {

    private GoodListAdapter goodListAdapter;


    public TaobaoListGoodConvertFunction(TaobaoObject rawObj) {
        this.goodListAdapter = new GoodListAdapter(rawObj);
    }


    public ListGood convert() {
        ListGood good = new ListGood();
        good.setItemId(goodListAdapter.getItemId());
        good.setGoodTitle(goodListAdapter.getTitle());
        good.setDirect(false);
        good.setCurrPrice(getCurrPrice(goodListAdapter));
        good.setOriginalPrice(getOriginalPrice(goodListAdapter));
        if (StringUtils.isEmpty(goodListAdapter.getCouponAmount()) && goodListAdapter.getCouponAmount() != 0L) {
            good.setOfferInfo(new OfferInfo(getStrOfferInfo(goodListAdapter)));
        } else {
            StringJoiner joiner = new StringJoiner("~");
            if (!StringUtils.isEmpty(goodListAdapter.getCouponStartTime())) {
                joiner.add(Dates.format(Long.valueOf(goodListAdapter.getCouponStartTime())));
            }
            if (!StringUtils.isEmpty(goodListAdapter.getCouponEndTime())) {
                joiner.add(Dates.format(Long.valueOf(goodListAdapter.getCouponEndTime())));
            }
            good.setOfferInfo(new OfferInfo(new CouponInfo(String.valueOf(goodListAdapter.getCouponAmount()), joiner.toString())));
        }
        good.setImage("http:" + goodListAdapter.getPictUrl());
        if (!StringUtils.isEmpty(goodListAdapter.getSellNum())) {
            good.setSellNum(goodListAdapter.getSellNum());
        }
        if (!StringUtils.isEmpty(goodListAdapter.getVolume())) {
            good.setSellNum(String.valueOf(goodListAdapter.getVolume()));
        }
        good.setImages(goodListAdapter.getSmallImages()
                .stream()
                .map((image) -> "http:" + image)
                .collect(Collectors.toList()));
        return good;
    }

    static class GoodListAdapter {

        TbkDgOptimusMaterialResponse.MapData materialData;

        TbkDgMaterialOptionalResponse.MapData optionalData;


        public GoodListAdapter(TaobaoObject rawObj) {
            if (rawObj instanceof TbkDgOptimusMaterialResponse.MapData) {
                materialData = (TbkDgOptimusMaterialResponse.MapData) rawObj;
            } else {
                optionalData = (TbkDgMaterialOptionalResponse.MapData) rawObj;
            }
        }

        public Long getItemId() {
            if (materialData != null) {
                return materialData.getItemId();
            } else {
                return optionalData.getItemId();
            }

        }

        public String getTitle() {
            if (materialData != null) {
                return materialData.getTitle();
            } else {
                return optionalData.getTitle();
            }
        }

        public String getJhsPriceUspList() {
            if (materialData != null) {
                return materialData.getJhsPriceUspList();
            } else {
                return "";
            }
        }

        public List<String> getSmallImages() {
            if (materialData != null) {
                return materialData.getSmallImages();
            } else {
                return optionalData.getSmallImages();
            }
        }

        public Long getVolume() {
            if (materialData != null) {
                return materialData.getVolume();
            } else {
                return optionalData.getVolume();
            }
        }

        public Long getCouponAmount() {
            if (materialData != null) {
                return materialData.getCouponAmount();
            } else {
                return Long.valueOf(optionalData.getCouponAmount());
            }
        }

        public String getItemDescription() {
            if (materialData != null) {
                return materialData.getItemDescription();
            } else {
                return optionalData.getItemDescription();
            }
        }

        public String getCouponStartTime() {
            if (materialData != null) {
                return materialData.getCouponStartTime();
            } else {
                return optionalData.getCouponStartTime();
            }
        }

        public String getCouponEndTime() {
            if (materialData != null) {
                return materialData.getCouponEndTime();
            } else {
                return optionalData.getCouponEndTime();
            }
        }

        public String getPictUrl() {
            if (materialData != null) {
                return materialData.getPictUrl();
            } else {
                return optionalData.getPictUrl();
            }
        }

        public String getSellNum() {
            if (materialData != null) {
                return String.valueOf(materialData.getSellNum());
            } else {
                return String.valueOf(optionalData.getSellNum());
            }
        }


        public String getZkFinalPrice() {
            if (materialData != null) {
                return materialData.getZkFinalPrice();
            } else {
                return optionalData.getZkFinalPrice();
            }
        }

        public String getReservePrice() {
            if (materialData != null) {
                return materialData.getReservePrice();
            } else {
                return optionalData.getReservePrice();
            }
        }

        public String getOrigPrice() {
            if (materialData != null) {
                return materialData.getOrigPrice();
            } else {
                return optionalData.getOrigPrice();
            }
        }
    }


    private String getOriginalPrice(GoodListAdapter item) {
        if (!StringUtils.isEmpty(item.getOrigPrice())) {
            return item.getOrigPrice();
        }
        if (!StringUtils.isEmpty(item.getZkFinalPrice()) && !StringUtils.isEmpty(item.getCouponAmount())) {
            return item.getZkFinalPrice();
        }
        return item.getReservePrice();
    }

    private String getCurrPrice(GoodListAdapter item) {
        if (!StringUtils.isEmpty(item.getZkFinalPrice())) {
            if (!StringUtils.isEmpty(item.getCouponAmount())) {
                return String.valueOf(new BigDecimal(item.getZkFinalPrice()).subtract(new BigDecimal(item.getCouponAmount())));
            }
            return item.getZkFinalPrice();
        }
        return item.getReservePrice();
    }


    private String getStrOfferInfo(GoodListAdapter item) {
        StringBuffer rst = new StringBuffer();
        //聚划算优惠描述
        if (!StringUtils.isEmpty(item.getJhsPriceUspList())) {
            rst.append(item.getJhsPriceUspList());
        }
        if (!StringUtils.isEmpty(item.getItemDescription())) {
            rst.append(item.getItemDescription());
        }
        if (!StringUtils.isEmpty(item.getZkFinalPrice())) {
            rst.append(" 折扣价【").append(item.getZkFinalPrice()).append("】");
        }
        return rst.toString();
    }

}
