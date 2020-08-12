package com.normal.bizassistant.autosend;

import java.util.List;

public interface SendGoodResponse {
    /**
     * 商品文案:包含淘口令
     * @return
     */
    String text();

    /**
     * 商品图片
     * @return
     */
    List<Byte[]> images();

}
