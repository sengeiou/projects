package com.normal.bizassistant.autosend;

import lombok.Data;

import java.util.List;

/**
 * @author fei.he
 */
@Data
public class SendGood {
    /**
     * 商品文案:包含淘口令
     * @return
     */
   private String text;

    /**
     * 商品图片列表
     * @return
     */
    private List<byte[]> images;

}
