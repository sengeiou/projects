package com.normal.bizmodel;

/**
 * @author: fei.he
 */
public interface ClientRecvListener {

    String code();


    void recv(DuplexMsg rst);
}
