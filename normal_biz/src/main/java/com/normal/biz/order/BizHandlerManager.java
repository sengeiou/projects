package com.normal.biz.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizmodel.RecvResult;
import com.normal.bizmodel.ServerRecvListener;
import com.normal.core.web.Result;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fei.he
 */
public class BizHandlerManager extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final Logger logger = LoggerFactory.getLogger(BizHandlerManager.class);

    private ChannelGroup channelGroup;
    private ObjectMapper objectMapper;
    private ConcurrentHashMap<String, ServerRecvListener> serverRecvRegistry;

    public BizHandlerManager(ChannelGroup channelGroup, ObjectMapper objectMapper, ConcurrentHashMap<String, ServerRecvListener> serverRecvRegistry) {
        this.channelGroup = channelGroup;
        this.objectMapper = objectMapper;
        this.serverRecvRegistry = serverRecvRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        logger.info("recv msg: channel = {}, msg = {}", ctx.channel().toString(), msg.text());
        if (!StringUtils.isEmpty(text)) {
            RecvResult recvResult = objectMapper.readValue(text, RecvResult.class);
            if (StringUtils.isEmpty(recvResult.getCode()) || StringUtils.isEmpty(recvResult.getData())) {
                logger.info("server income msg error, recvRst: {}", recvResult);
                return;
            }
            ServerRecvListener serverRecvListener = serverRecvRegistry.get(recvResult.getCode());
            if (serverRecvListener == null) {
                logger.warn("server listener empty for recvRst: {}", recvResult);
                return;
            }

            serverRecvListener.recv(recvResult.getData(), ctx);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            channelGroup.add(ctx.channel());
        }
        //read timeout
        if (evt instanceof IdleStateEvent) {
            boolean readTimeout = IdleState.READER_IDLE.equals(((IdleStateEvent) evt).state());
            if (readTimeout) {
                ctx.channel().close();
                logger.info("channel: {} read timeout closed");
            }
        }
        super.userEventTriggered(ctx, evt);
    }


}