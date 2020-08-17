package com.normal.bizassistant.autosend;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author fei.he
 */
@Data
@ToString
public class SendGood {
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

}
