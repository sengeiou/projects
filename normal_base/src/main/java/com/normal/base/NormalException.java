package com.normal.base;

/**
 * @author: fei.he
 */
public class NormalException extends  Exception {


    public NormalException(String message) {
        super(message);
    }

    public NormalException(String message, Throwable cause) {
        super(message, cause);
    }

    public NormalException(Throwable cause) {
        super(cause);
    }
}
