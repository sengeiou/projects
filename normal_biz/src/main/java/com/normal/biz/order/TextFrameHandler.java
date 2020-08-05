package com.normal.biz.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizmodel.Order;
import com.normal.bizmodel.OrderStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author fei.he
 */
public class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final Logger logger = LoggerFactory.getLogger(TextFrameHandler.class);

    private ChannelGroup channelGroup;
    private ObjectMapper objectMapper;
    private ClientListener listener;

    public TextFrameHandler(ChannelGroup channelGroup, ObjectMapper objectMapper, ClientListener listener) {
        this.channelGroup = channelGroup;
        this.objectMapper = objectMapper;
        this.listener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        logger.info("接收到客户端信息: channel = {}, msg = {}", ctx.channel().toString(), msg.text());
        if (!StringUtils.isEmpty(text)) {
            Order order = objectMapper.readValue(text, Order.class);
            if (order == null) {
                return;
            }
            if (OrderStatus.PAIED.equals(order.getOrderStatus())) {
                listener.onPaid(order);
            }
            if (OrderStatus.TIMEOUT.equals(order.getOrderStatus())) {
                listener.onPayTimeout(order);
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            channelGroup.add(ctx.channel());
            return;
        }
        super.userEventTriggered(ctx, evt);
    }
}