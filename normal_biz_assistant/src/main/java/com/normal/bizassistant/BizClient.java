package com.normal.bizassistant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizmodel.ClientRecvListener;
import com.normal.bizmodel.DuplexMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: fei.he
 */
public class BizClient extends SimpleChannelInboundHandler<TextWebSocketFrame> implements IBizClientService {

    public static final Logger logger = LoggerFactory.getLogger(BizClient.class);
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final NioEventLoopGroup group;

    private Bootstrap bootstrap = new Bootstrap();
    private ObjectMapper objectMapper = new ObjectMapper();
    private ConcurrentHashMap<String, ClientRecvListener> clientRecvRegistry = new ConcurrentHashMap<>(8);

    public BizClient() {
        this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConfigProperties.getConnectTimeoutMillis());
        this.group = new NioEventLoopGroup();
        this.bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new WebSocketClientProtocolHandler(URI.create(ConfigProperties.getBizClientConnectUri()), WebSocketVersion.V13, "http", false, EmptyHttpHeaders.INSTANCE, 1024 * 6, false, false, false));
                        ch.pipeline().addLast(BizClient.this);
                    }
                });
    }

    public void close() {
        group.shutdownGracefully();
    }

    @Override
    public void send(DuplexMsg msg) {
        if (channelGroup.isEmpty()) {
            throw new RuntimeException("未连接服务端");
        }
        try {
            channelGroup.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(msg)));
        } catch (JsonProcessingException e) {
            logger.error("e:{}", e);
        }
    }

    @Override
    public void addListener(ClientRecvListener listener) {
        clientRecvRegistry.putIfAbsent(listener.code(), listener);
        logger.info("client listener added success, code:{}", listener.code());
    }

    @Override
    public void removeListener(String code) {
        clientRecvRegistry.remove(code);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        if (!StringUtils.isEmpty(text)) {
            return;
        }
        DuplexMsg recvMsg = objectMapper.readValue(text, DuplexMsg.class);

        ClientRecvListener clientRecvListener = clientRecvRegistry.get(recvMsg.getCode());
        if (clientRecvListener == null) {
            logger.info("client listener is empty for msg:{}", recvMsg);
            return;
        }
        clientRecvListener.recv(recvMsg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean handShakeCompleted = evt.equals(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
        if (handShakeCompleted) {
            channelGroup.add(ctx.channel());
        }
        super.userEventTriggered(ctx, evt);
    }

    public static void main(String[] args) throws Exception{
        ConfigProperties.load();
        new BizClient();
    }
}
