package com.normal.model.openapi;

import lombok.Data;

import java.util.List;

/**
 * @author: fei.he
 */
@Data
public class AppGood {

    private Long categoryId;

    private String goodTitle;

    private String goodSubTitle;

    /**
     * 优惠描述
     */
    private String offerInfo;


    private List<String> images;


}
