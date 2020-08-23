package com.normal.model.autosend;

import com.normal.model.YesOrNoEnum;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fei.he
 */
@Data
@ToString
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
}
