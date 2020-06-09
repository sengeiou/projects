package com.normal.core.web;


public enum CommonErrorMsg {

    RUNTIME_ERROR("-1000", "运行时异常"),

    ILLEGE_ARG("-1001", "参数非法");

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
