package com.normal.model.autosend;

import com.normal.model.YesOrNoEnum;



import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fei.he
 */


public class SendGood {
    private Integer id;
    /**
     * 商品id
     */
    private Long categoryId;
    /**
     * 商品文案:包含淘口令
     * @return
     */
   private String text;


    /**
     * 商品图片目录列表
     * @return
     */
    private List<String> imagePaths;

    /**
     * 是否发送标识
     */
    private YesOrNoEnum status;


    public String getImageStrs() {
        if (imagePaths != null) {
            return imagePaths.stream().collect(Collectors.joining(","));
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public YesOrNoEnum getStatus() {
        return status;
    }

    public void setStatus(YesOrNoEnum status) {
        this.status = status;
    }
}
