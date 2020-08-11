package com.normal.bizserver.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizmodel.*;
import com.normal.core.web.Result;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fei.he
 */
public class OrderServiceImpl implements IOrderService, ServerRecvListener {

    public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PriceGenerator priceGenerator;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void recv(DuplexMsg msg, ChannelHandlerContext ctx) {
        Order order = null;
        try {
            order = objectMapper.readValue(msg.getData(), Order.class);
            if (msg.getCode().equals(BizCodes.PAID_FINISHED)) {
                orderMapper.updateOrderStatus(order.getId(), OrderStatus.PAIED);
            }
            if (msg.getCode().equals(BizCodes.PAID_TIMEOUT)) {
                orderMapper.updateOrderStatus(order.getId(), OrderStatus.TIMEOUT);
            }
        } catch (JsonProcessingException e) {
            logger.error("e:{}", e);
        }

    }

    @Override
    public String[] codes() {
        return new String[]{BizCodes.PAID_TIMEOUT, BizCodes.PAID_FINISHED};
    }


}
