package com.normal.biz.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.core.web.CommonErrorMsg;
import com.normal.core.web.Result;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class OrderServiceImpl implements IOrderService, ClientListener {

    public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private BizProperties properties;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    /**
     * 已连接的客户端channel
     */
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    @PostConstruct
    public void initServer() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        bossGroup = new EpollEventLoopGroup();
        workGroup = new EpollEventLoopGroup(properties.getWorkThreadNum());
        b.group(bossGroup, workGroup).channel(EpollServerSocketChannel.class)
                .localAddress(new InetSocketAddress(properties.getPort()))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new IdleStateHandler(properties.getReadTimeout(), 0, 0),
                                new HttpServerCodec(),
                                new HttpObjectAggregator(65536),
                                //websocket 升级协议.
                                new WebSocketServerProtocolHandler("/ws"),
                                new TextFrameHandler(channelGroup, objectMapper, OrderServiceImpl.this));
                    }
                });
        b.bind().syncUninterruptibly();

        logger.info("server started !");

    }


    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @Override
    public Result createOrder(Order order) {
        if (channelGroup.isEmpty()) {
            logger.warn("还没有 channel 连上来, 创建订单失败");
            return Result.fail(CommonErrorMsg.ILLEGE_STATE);
        }
        orderMapper.insertSelective(order);
        try {
            String json = objectMapper.writeValueAsString(order);
            channelGroup.writeAndFlush(json);
            return Result.success();
        } catch (Exception e) {
            logger.error("e: {}", e);
            return Result.fail(CommonErrorMsg.RUNTIME_ERROR);
        }
    }


    @Override
    public void onPaid(Order order) {

    }

    @Override
    public void onPayTimeout(Order order) {

    }
}
