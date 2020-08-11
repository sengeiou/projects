package com.normal.bizserver;

import com.normal.bizmodel.ServerRecvListener;

/**
 * @author: fei.he
 */
public interface IBizServerService {

    void dispatch(Object msg);

    void addListener(ServerRecvListener listener);

    void removeListener(String code);
}
