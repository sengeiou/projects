package com.normal.devtool.dbupdate;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
@Data
public class ParseNode {

    private String name;

    private String id;

    private Map<String, Object> attributes;

    private List<ParseNode> children;


}
