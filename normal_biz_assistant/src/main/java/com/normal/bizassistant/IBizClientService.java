package com.normal.bizassistant;

import com.normal.bizmodel.ClientRecvListener;

/**
 * @author: fei.he
 */
public interface IBizClientService {

    void send(Object msg);

    void addListener(ClientRecvListener listener);

    void removeListener(String code);
}
