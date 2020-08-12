package com.normal.bizserver;

import com.normal.bizmodel.DuplexMsg;
import com.normal.bizmodel.ServerRecvListener;

/**
 * @author: fei.he
 */
public interface IBizServerService {

    void dispatch(DuplexMsg msg);

    void addListener(ServerRecvListener listener);

    void removeListener(String code);
}
