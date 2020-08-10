package com.normal.bizassistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizmodel.ClientRecvListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import javax.annotation.PostConstruct;

/**
 * @author: fei.he
 */
public class BizClient implements IBizClientService {

    private Bootstrap bootstrap;
    private ObjectMapper objectMapper = new ObjectMapper();

    public BizClient() {
        this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConfigProperties.getConnectTimeoutMillis());

        this.bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new IdleStateHandler());
                    }
                });
    }

    @Override
    public void send(Object msg) {

    }

    @Override
    public void addListener(ClientRecvListener listener) {

    }

    @Override
    public void removeListener(String code) {

    }
}
