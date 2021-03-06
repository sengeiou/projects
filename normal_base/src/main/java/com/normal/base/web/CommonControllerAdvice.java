package com.normal.base.web;

import com.normal.base.NormalRuntimeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackageClasses = BaseController.class)
public class CommonControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    public Result exceptionHandler(Throwable throwable) {
        if (throwable instanceof NormalRuntimeException) {
            return Result.fail(((NormalRuntimeException) throwable).getErrorMsg());
        }
        return Result.fail(CommonErrorMsg.RUNTIME_ERROR.getCode(), throwable.getMessage());
    }

}
