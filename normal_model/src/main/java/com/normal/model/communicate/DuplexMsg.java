package com.normal.model.communicate;




/**
 * @author: fei.he
 */


public class DuplexMsg {

    private String code;

    private String data;

    public DuplexMsg(String code, String data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
