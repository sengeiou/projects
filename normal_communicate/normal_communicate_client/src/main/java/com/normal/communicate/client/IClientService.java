package com.normal.communicate.client;

import com.normal.model.communicate.DuplexMsg;

/**
 * @author: fei.he
 */
public interface IClientService {

    void send(DuplexMsg msg);

    void addListener(ClientRecvListener listener);

    void removeListener(String code);
}
