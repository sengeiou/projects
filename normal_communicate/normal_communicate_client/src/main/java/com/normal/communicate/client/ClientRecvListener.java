package com.normal.communicate.client;

import com.normal.model.communicate.DuplexMsg;

/**
 * @author: fei.he
 */
public interface ClientRecvListener {

    String[] codes();


    void recv(DuplexMsg rst);
}
