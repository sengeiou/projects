package com.normal.bizassistant;

import com.normal.bizmodel.ClientRecvListener;
import com.normal.bizmodel.DuplexMsg;

/**
 * @author: fei.he
 */
public interface IBizClientService {

    void send(DuplexMsg msg);

    void addListener(ClientRecvListener listener);

    void removeListener(String code);
}
