package com.normal.base;

import com.normal.base.web.CommonErrorMsg;

public class NormalRuntimeException extends RuntimeException {

    private CommonErrorMsg errorMsg;

    public NormalRuntimeException(Throwable cause) {
        super(cause);
    }

    public NormalRuntimeException(String message, CommonErrorMsg errorMsg) {
        super(message);
        this.errorMsg = errorMsg;
    }

    public CommonErrorMsg getErrorMsg() {
        return errorMsg;
    }
}
