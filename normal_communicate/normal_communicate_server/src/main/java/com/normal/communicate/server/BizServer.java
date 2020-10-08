package com.normal.communicate.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.model.*;
import com.normal.base.utils.Jsons;
import com.normal.model.communicate.DuplexMsg;
import com.normal.model.order.Order;
import com.normal.model.order.OrderStatus;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: fei.he
 */
public class BizServer extends SimpleChannelInboundHandler<TextWebSocketFrame> implements IBizServerService {

    public static final Logger logger = LoggerFactory.getLogger(BizServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    @Autowired
    private BizProperties properties;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 已连接的客户端channel
     */
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private ConcurrentHashMap<String, ServerRecvListener> serverRecvRegistry = new ConcurrentHashMap<>(8);

    @PostConstruct
    public void initServer() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup(properties.getWorkThreadNum());
        b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new HttpObjectAggregator(65536));
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
                        ch.pipeline().addLast(BizServer.this);
                    }
                });
        b.bind(properties.getPort()).syncUninterruptibly();

        logger.info("server started !");

    }


    @PreDestroy
    public void close() {
        channelGroup.close();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }


    @Override
    public void dispatch(DuplexMsg msg) {
        try {
            if (channelGroup.isEmpty()) {
                throw new RuntimeException("还未有客户端连接");
            }
            channelGroup.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(msg)));
        } catch (JsonProcessingException e) {
            logger.error("json parse error");
        }
    }

    @Override
    public void addListener(ServerRecvListener listener) {
        for (String code : listener.codes()) {
            serverRecvRegistry.putIfAbsent(code, listener);
        }
    }

    @Override
    public void removeListener(String code) {
        serverRecvRegistry.remove(code);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        logger.info("recv msg: channel = {}, msg = {}", ctx.channel().toString(), msg.text());
        if (!StringUtils.isEmpty(text)) {
            DuplexMsg recvResult = objectMapper.readValue(text, DuplexMsg.class);
            if (StringUtils.isEmpty(recvResult.getCode()) || StringUtils.isEmpty(recvResult.getData())) {
                logger.info("server income msg error, recvRst: {}", recvResult);
                return;
            }
            ServerRecvListener serverRecvListener = serverRecvRegistry.get(recvResult.getCode());
            if (serverRecvListener == null) {
                logger.warn("server listener empty for recvRst: {}", recvResult);
                return;
            }

            serverRecvListener.recv(recvResult, ctx);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            channelGroup.add(ctx.channel());
            logger.info("server hand shake completed, add channel");

            Order order = new Order();
            order.setId(1L);
            order.setOrderStatus(OrderStatus.NEW);
            this.dispatch(new DuplexMsg(BizCodes.ORDER_QUERY_ALIPAY_ORDERS, Jsons.toJson(order)));
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel: {} registered ", ctx.channel());
        super.channelRegistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server happen exception:{}", cause);
        ctx.close();
    }
}
