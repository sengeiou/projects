package com.normal.model.context;

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
