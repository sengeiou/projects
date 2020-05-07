package com.normal.core.web;

public class Result {

    private String code;
    private String msg;
    private boolean success;
    private Object data;

    private Result(String code, String msg, boolean success, Object data) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public static Result success(Object data) {
        return new Result("200", null, true, data);
    }
    public static Result fail(String code , String msg) {
        return new Result(code, msg, false, null);
    }


}