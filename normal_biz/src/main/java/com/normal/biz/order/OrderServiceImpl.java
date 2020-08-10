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
public class OrderServiceImpl implements IOrderService, ClientListener {

    public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);



    @Autowired
    private OrderMapper orderMapper;


    @Autowired
    private PriceGenerator priceGenerator;


    @Override
    @Transactional
    public Result createOrder(Order order) {
        Map<String, Object> rstData = new HashMap<>(2);
        Double nextPrice = priceGenerator.gen(order.getPrice().doubleValue());
        order.setPrice(BigDecimal.valueOf(nextPrice));
        orderMapper.insertSelective(order);
        rstData.put("order", order);
        return null;
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
