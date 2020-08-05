package com.normal.biz.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizmodel.Order;
import com.normal.bizmodel.OrderStatus;
import com.normal.core.NormalException;
import com.normal.core.web.CommonErrorMsg;
import com.normal.core.web.Result;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fei.he
 */
@Component
public class OrderServiceImpl implements IOrderService, ClientListener {

    public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private BizProperties properties;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PriceGenerator priceGenerator;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;


    /**
     * 已连接的客户端channel
     */
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    @PostConstruct
    public void initServer() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup(properties.getWorkThreadNum());
        b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
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
    @Transactional
    public Result createOrder(Order order) {
        Map<String, Object> rstData = new HashMap<>(2);
        if (channelGroup.isEmpty()) {
            logger.warn("还没有 channel 连上来, 创建订单失败");
            return Result.fail(CommonErrorMsg.ILLEGE_STATE);
        }
        Double nextPrice = priceGenerator.gen(order.getPrice().doubleValue());
        order.setPrice(BigDecimal.valueOf(nextPrice));
        orderMapper.insertSelective(order);
        rstData.put("order", order);
        try {
            String json = objectMapper.writeValueAsString(order);
            ChannelGroupFuture future = channelGroup.writeAndFlush(json).sync();
            if (future.isSuccess()) {
                StringBuffer qrCode = new StringBuffer("statics/")
                        .append(nextPrice)
                        .append(".png");
                File qrCodeFile = ResourceUtils.getFile("classpath:statics/" + qrCode.toString());
                if (qrCodeFile.exists()) {
                    rstData.put("url", qrCode.toString());
                    return Result.success(rstData);
                }
                rstData.put("url", properties.getPriceQrCode());
                return Result.success(rstData);
            }
        } catch (JsonProcessingException | InterruptedException | FileNotFoundException e) {
            logger.error("e: {}", e);
            if (e instanceof FileNotFoundException) {
                rstData.put("url", properties.getPriceQrCode());
                return Result.success(rstData);
            }
        }
        throw new NormalException("未知异常", CommonErrorMsg.RUNTIME_ERROR);
    }

    @Override
    public Result queryOrder(long id) {
        Order order = orderMapper.selectByPrimaryKey(Long.valueOf(id).intValue());
        return Result.success(order);
    }



    @Override
    public void onPaid(Order order) {
        orderMapper.updateOrderStatus(order.getId(), OrderStatus.PAIED);
    }

    @Override
    public void onPayTimeout(Order order) {
        orderMapper.updateOrderStatus(order.getId(), OrderStatus.TIMEOUT);
    }
}
