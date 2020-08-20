package com.normal.communicate.server;

import com.normal.model.communicate.DuplexMsg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: fei.he
 */
public interface ServerRecvListener {

    void recv(DuplexMsg msg, ChannelHandlerContext ctx);

    String[] codes();

}
