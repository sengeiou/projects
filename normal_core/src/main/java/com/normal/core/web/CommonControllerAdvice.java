package com.normal.core.web;

import com.normal.core.NormalException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    public Result exceptionHandler(Throwable throwable) {
        if (throwable instanceof NormalException) {
            return Result.fail(((NormalException) throwable).getErrorMsg());
        }
        return Result.fail(CommonErrorMsg.RUNTIME_ERROR.getCode(), throwable.getMessage());
    }

}
