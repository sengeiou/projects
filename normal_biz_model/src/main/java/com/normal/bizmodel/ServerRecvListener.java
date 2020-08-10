package com.normal.bizmodel;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author: fei.he
 */
public interface ServerRecvListener {

    void recv(Object msg, ChannelHandlerContext ctx);

    String code();

}
