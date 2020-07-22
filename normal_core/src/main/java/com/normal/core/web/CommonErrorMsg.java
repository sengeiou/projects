package com.normal.core.web;


public enum CommonErrorMsg {

    RUNTIME_ERROR("-1000", "运行时异常"),
    AUTH_ERROR("-1002", "无权限"),

    ILLEGE_ARG("-1001", "参数非法"),
    ILLEGE_STATE("-1002", "状态非法");

    private final String code;

    private final String msg;

    CommonErrorMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
