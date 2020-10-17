package com.normal.base.web;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fei.he
 */
@Getter
@Setter
public class Result<T> {

    private String code;
    private String msg;
    private boolean success;
    private T data;

    private Result(String code, String msg, boolean success, T data) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public static Result success(Object data) {
        return new Result("200", null, true, data);
    }

    public static Result success() {
        return new Result("200", null, true, null);
    }

    public static Result fail(String code, String msg) {
        return new Result(code, msg, false, null);
    }

    public static Result fail(CommonErrorMsg errorMsg) {
        return new Result(errorMsg.getCode(), errorMsg.getMsg(), false, null);
    }


}
