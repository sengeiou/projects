package com.normal.bizmodel;

/**
 * @author: fei.he
 */
public interface ClientRecvListener {

    String[] codes();


    void recv(DuplexMsg rst);
}
