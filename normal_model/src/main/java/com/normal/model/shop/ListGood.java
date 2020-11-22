package com.normal.model.shop;



import java.util.List;

/**
 * @author: fei.he
 */

public class ListGood {

    private Long  itemId;

    private String goodTitle;

    /**
     * 原价
     */
    private String originalPrice;

    /**
     * 当前价格
     */
    private String currPrice;

    /**
     * 优惠描述:可能是优惠券/满减
     */
    private OfferInfo offerInfo;

    /**
     * 主图
     */
    private String  image;

    /**
     * 销量
     */
    private String sellNum;

    /**
     * 是否直接领券
     */
    private boolean direct = false;

    /**
     * 淘口令:淘宝客用
     */
    private String tbPwd;

    /**
     * 子图片列表
     */
    private List<String> images;

    /**
     *  所属平台
     */
    private String platform;

    /**
     * tb 券优惠领取链接
     */
    private String tbShareUrl;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getGoodTitle() {
        return goodTitle;
    }

    public void setGoodTitle(String goodTitle) {
        this.goodTitle = goodTitle;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getCurrPrice() {
        return currPrice;
    }

    public void setCurrPrice(String currPrice) {
        this.currPrice = currPrice;
    }

    public OfferInfo getOfferInfo() {
        return offerInfo;
    }

    public void setOfferInfo(OfferInfo offerInfo) {
        this.offerInfo = offerInfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSellNum() {
        return sellNum;
    }

    public void setSellNum(String sellNum) {
        this.sellNum = sellNum;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public String getTbPwd() {
        return tbPwd;
    }

    public void setTbPwd(String tbPwd) {
        this.tbPwd = tbPwd;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTbShareUrl() {
        return tbShareUrl;
    }

    public void setTbShareUrl(String tbShareUrl) {
        this.tbShareUrl = tbShareUrl;
    }
}
