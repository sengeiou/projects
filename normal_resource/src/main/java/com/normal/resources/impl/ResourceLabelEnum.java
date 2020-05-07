package com.normal.resources.impl;

/**
 * 资源标签: 最大 32 个枚举
 */
public enum ResourceLabelEnum {

    KZCL("考证材料"),


    KCSJ("课程设计");

    private final String value;

    ResourceLabelEnum(String value) {
        this.value = value;
    }

    /**
     * @return 该资源类型对应 int 的第几个bit位
     */
    public int getBitIdx() {
        return this.ordinal();
    }

}
