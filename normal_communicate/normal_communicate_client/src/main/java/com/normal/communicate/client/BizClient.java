package com.normal.communicate.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author: fei.he
 */
public class BizClient {

    public static final Logger logger = LoggerFactory.getLogger(BizClient.class);

    private final NioEventLoopGroup group;
    private final BizClientHandler bizClientHandler;
    private Bootstrap bootstrap = new Bootstrap();


    public BizClient() {
        this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConfigProperties.getConnectTimeoutMillis());
        this.group = new NioEventLoopGroup();
        WebSocketClientHandshaker shaker = WebSocketClientHandshakerFactory.newHandshaker(
                URI.create(ConfigProperties.getBizClientConnectUri()), WebSocketVersion.V13, null, false, new DefaultHttpHeaders());
        this.bizClientHandler = new BizClientHandler(shaker);
        this.bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new HttpClientCodec());
                        ch.pipeline().addLast(new HttpObjectAggregator(8192));
                        ch.pipeline().addLast(bizClientHandler);
                    }
                });

        ChannelFuture future = bootstrap.connect(ConfigProperties.getClientConnectIp(), ConfigProperties.getClientConnectPort()).syncUninterruptibly();
        future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    public void close() {
        bizClientHandler.close();
        group.shutdownGracefully();
    }

    public BizClientHandler getBizClientHandler() {
        return bizClientHandler;
    }
}
