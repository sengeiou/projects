package com.normal.bizmodel;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author: fei.he
 */
public interface ServerRecvListener {

    void recv(DuplexMsg msg, ChannelHandlerContext ctx);

    String[] codes();

}
