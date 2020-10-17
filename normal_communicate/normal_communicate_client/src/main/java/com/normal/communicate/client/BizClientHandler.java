package com.normal.communicate.client;

import com.normal.model.communicate.DuplexMsg;
import com.normal.base.utils.Objs;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fei.he
 */
public class BizClientHandler extends SimpleChannelInboundHandler<Object> implements IClientService {

    public static final Logger logger = LoggerFactory.getLogger(BizClientHandler.class);

    private final WebSocketClientHandshaker handShaker;
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final ConcurrentHashMap<String, ClientRecvListener> clientRecvRegistry = new ConcurrentHashMap<>(8);

    private ChannelPromise handshakeFuture;

    public BizClientHandler(WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("channel active hand shake");
        handShaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("WebSocket Client disconnected!");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handShaker.isHandshakeComplete()) {
            try {
                handShaker.finishHandshake(ch, (FullHttpResponse) msg);
                logger.info("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                logger.info("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.getStatus() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            logger.info("WebSocket Client received message: " + textFrame.text());
            String text = textFrame.text();
            if (!StringUtils.isEmpty(text)) {
                return;
            }
            DuplexMsg recvMsg = Objs.toObj(text, DuplexMsg.class);

            ClientRecvListener clientRecvListener = clientRecvRegistry.get(recvMsg.getCode());
            if (clientRecvListener == null) {
                logger.info("client listener is empty for msg:{}", recvMsg);
                return;
            }
            clientRecvListener.recv(recvMsg);
        } else if (frame instanceof PongWebSocketFrame) {
            logger.info("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            logger.info("WebSocket Client received closing");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("e:{}", cause);
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

    @Override
    public void send(DuplexMsg msg) {
        if (channelGroup.isEmpty()) {
            throw new RuntimeException(" client channel group empty can not send msg!");
        }
        channelGroup.writeAndFlush(new TextWebSocketFrame(Objs.toJson(msg)));
    }

    @Override
    public void addListener(ClientRecvListener listener) {
        for (String code : listener.codes()) {
            clientRecvRegistry.putIfAbsent(code, listener);
        }
    }

    @Override
    public void removeListener(String code) {
        clientRecvRegistry.remove(code);
    }

    public void close() {
        if (channelGroup != null && !channelGroup.isEmpty()) {
            channelGroup.close();
        }
    }
}