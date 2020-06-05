package com.normal.resources.impl;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 资源标签: 最大 32 个枚举
 */
public enum ResourceLabelEnum {

    KZCL("考证材料"),


    KCSJ("课程设计");

    @JsonValue
    private final String value;

    ResourceLabelEnum(String value) {
        this.value = value;
    }

    /**
     * @return 该资源类型对应 int 的第几个bit位
     */
    public int getBitIdx() {
        return this.ordinal() + 1;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    public static List<Map> getDisplayLabels() {

        return Stream.of(ResourceLabelEnum.values()).map(item -> {
            Map<String, Object> result = new HashMap<>();
            result.put("idx", item.getBitIdx());
            result.put("text", item.getValue());
            return result;
        }).collect(Collectors.toList());
    }
}
