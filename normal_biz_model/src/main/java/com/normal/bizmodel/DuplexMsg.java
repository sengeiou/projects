package com.normal.bizmodel;

import lombok.Data;
import lombok.ToString;

/**
 * @author: fei.he
 */
@Data
@ToString
public class DuplexMsg {

    private String code;

    private String data;

    public DuplexMsg(String code, String data) {
        this.code = code;
        this.data = data;
    }
}
