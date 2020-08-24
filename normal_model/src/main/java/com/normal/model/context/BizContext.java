package com.normal.model.context;

import lombok.Data;

import java.util.Map;

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
