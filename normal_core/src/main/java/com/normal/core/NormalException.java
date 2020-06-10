package com.normal.core;

import com.normal.core.web.CommonErrorMsg;

public class NormalException extends RuntimeException {
    private CommonErrorMsg errorMsg;

    public NormalException(String message, CommonErrorMsg errorMsg) {
        super(message);
        this.errorMsg = errorMsg;
    }

    public CommonErrorMsg getErrorMsg() {
        return errorMsg;
    }
}
