package com.normal.communicate.server;

import com.normal.model.communicate.DuplexMsg;

/**
 * @author: fei.he
 */
public interface IBizServerService {

    void dispatch(DuplexMsg msg);

    void addListener(ServerRecvListener listener);

    void removeListener(String code);
}
