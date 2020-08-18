package com.normal.bizassistant;

import lombok.Data;

/**
 * @author: fei.he
 */
@Data
public class BizContext {

    private Integer id;
    /**
     * BizContextTypes
     */
    private String type;

    private String context;

}
