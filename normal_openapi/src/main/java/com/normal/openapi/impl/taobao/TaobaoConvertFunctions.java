package com.normal.openapi.impl.taobao;

import com.normal.base.utils.Dates;
import com.normal.base.utils.Files;
import com.normal.model.BizDictEnums;
import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.shop.CouponInfo;
import com.normal.model.shop.ListGood;
import com.normal.model.shop.OfferInfo;
import com.normal.openapi.impl.ClientWrapper;
import com.taobao.api.TaobaoObject;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class TaobaoConvertFunctions {

    public static final Logger logger = LoggerFactory.getLogger(TaobaoConvertFunctions.class);

    private GoodListAdapter goodListAdapter;


    private static final Pattern numPattern = Pattern.compile("[0-9]*");

    public TaobaoConvertFunctions(TaobaoObject rawObj) {
        this.goodListAdapter = new GoodListAdapter(rawObj);
    }

    private OfferInfo getOfferInfo() {
        if (!StringUtils.isEmpty(goodListAdapter.getCouponAmount()) && goodListAdapter.getCouponAmount() != 0L) {
            StringJoiner joiner = new StringJoiner("~");
            String couponStartTime = goodListAdapter.getCouponStartTime();
            joinCouponValidTime(joiner, couponStartTime);
            String couponEndTime = goodListAdapter.getCouponEndTime();
            joinCouponValidTime(joiner, couponEndTime);
            return new OfferInfo(new CouponInfo(String.valueOf(goodListAdapter.getCouponAmount()), joiner.toString()));

        } else {
            return new OfferInfo(getStrOfferInfo(goodListAdapter));
        }
    }

    public ListGood convertListGood() {
        ListGood good = new ListGood();
        good.setPlatform(BizDictEnums.PLATFORM_TB.key());
        good.setItemId(goodListAdapter.getItemId());
        good.setGoodTitle(goodListAdapter.getTitle());
        good.setDirect(false);
        good.setCurrPrice(getCurrPrice(goodListAdapter));
        good.setOriginalPrice(getOriginalPrice(goodListAdapter));
        good.setOfferInfo(getOfferInfo());
        good.setImage(goodListAdapter.getPictUrl());
        String couponShareUrl = goodListAdapter.getCouponShareUrl();
        if (StringUtils.isEmpty(couponShareUrl)) {
            good.setTbShareUrl(goodListAdapter.getCouponClickUrl());
        } else {
            good.setTbShareUrl(couponShareUrl);
        }
        if(StringUtils.isEmpty(good.getTbShareUrl())){
            good.setTbShareUrl(goodListAdapter.getClickUrl());
        }
        if (!StringUtils.isEmpty(goodListAdapter.getSellNum())) {
            good.setSellNum(goodListAdapter.getSellNum());
        }
        if (!StringUtils.isEmpty(goodListAdapter.getVolume())) {
            good.setSellNum(String.valueOf(goodListAdapter.getVolume()));
        }
        good.setImages(goodListAdapter.getSmallImages());
        return good;
    }


    public DailyNoticeItem covertDailyNoticeItem(ClientWrapper clientWrapper, Environment environment) {
        DailyNoticeItem noticeItem = new DailyNoticeItem();
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("商品: " + goodListAdapter.getTitle());
        joiner.add("原价: " + getOriginalPrice(goodListAdapter));
        String pwdUrl;
        if (!StringUtils.isEmpty(goodListAdapter.getCouponShareUrl())) {
            pwdUrl = goodListAdapter.getCouponShareUrl();
        } else if (!StringUtils.isEmpty(goodListAdapter.getCouponClickUrl())) {
            pwdUrl = goodListAdapter.getCouponClickUrl();
        } else {
            pwdUrl = goodListAdapter.getClickUrl();
        }
        joiner.add("淘口令: " + clientWrapper.queryTbPwd(pwdUrl));
        OfferInfo offerInfo = getOfferInfo();
        if (BizDictEnums.OFFER_TYPE_YHQ.equals(offerInfo.getOfferType())) {
            joiner.add("优惠券: " + offerInfo.getCouponInfo().getCouponAmt());
            joiner.add("有效期: " + offerInfo.getCouponInfo().getValidateDate());
        } else {
            joiner.add("优惠信息: " + offerInfo.getContext());
        }
        noticeItem.setText(joiner.toString());
        noticeItem.setImagePath(goodListAdapter.getImagePath(environment));
        return noticeItem;
    }

    private void joinCouponValidTime(StringJoiner joiner, String couponStartTime) {
        if (!StringUtils.isEmpty(couponStartTime)) {
            if (numPattern.matcher(couponStartTime).matches()) {
                joiner.add(Dates.format(Long.valueOf(couponStartTime)));
            } else {
                joiner.add(couponStartTime);
            }
        }
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

        public String getCouponShareUrl() {
            if (materialData != null) {
                return materialData.getCouponShareUrl();
            } else {
                return optionalData.getCouponShareUrl();
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
            List<String> smallImages = null;
            if (materialData != null) {
                if (materialData.getSmallImages() != null) {
                    smallImages = materialData.getSmallImages().stream().map((item) -> "http:" + item).collect(Collectors.toList());
                }
            } else {
                smallImages = optionalData.getSmallImages();
            }

            if (smallImages == null) {
                smallImages = Arrays.asList(getPictUrl());
            }
            return smallImages;
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
                if (optionalData.getCouponAmount() != null) {
                    return Long.valueOf(optionalData.getCouponAmount());
                } else {
                    return null;
                }
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
                return "http:" + materialData.getPictUrl();
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

        public String getCouponClickUrl() {
            if (materialData != null) {
                return materialData.getCouponClickUrl();
            } else {
                return optionalData.getUrl();
            }
        }

        public String getClickUrl() {
            if (materialData != null) {
                return materialData.getClickUrl();
            } else {
                return optionalData.getItemUrl();
            }
        }

        public String getImagePath(Environment environment) {
            if (materialData != null) {
                URL url = null;
                try {
                    url = new URL("http:" + materialData.getPictUrl());
                    Map<String, String> imageRst = TaobaoConvertFunctions.getGoodsPicPath(materialData.getPictUrl(), materialData.getCategoryId(), environment);
                    TaobaoConvertFunctions.downloadImage(url, imageRst.get("picPath"));
                    return imageRst.get("picPath");
                } catch (MalformedURLException e) {
                    //ignore
                    logger.error(e.getMessage());
                }
            } else {
                throw new UnsupportedOperationException("关键词搜索无需保存图片到本地");
            }
            return null;
        }
    }

    public static Map<String, String> getGoodsPicPath(String pictUrl, Long categoryId, Environment environment) {
        Map<String, String> rst = new HashMap<>(2);
        for (int j = pictUrl.length() - 1; j > 0; j--) {
            if (pictUrl.charAt(j) == '.') {
                String picName = categoryId + pictUrl.substring(j);
                rst.put("picPath", environment.getProperty("autosend.images.path") + picName);
                rst.put("picName", picName);
                return rst;
            }
        }
        return null;


    }

    public static void downloadImage(URL url, String picLocation) {
        File file = new File(picLocation);
        if (!file.exists()) {
            Files.download(url, picLocation);
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
            rst.append(" 折扣价【").append(getCurrPrice(item)).append("】");
        }
        return rst.toString();
    }

}
