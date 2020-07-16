package com.normal.biz.order;

import com.normal.core.web.Result;
import io.netty.buffer.ByteBuf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements  IOrderService {

    @Autowired


    @Override
    public Result createOrder(Order order) {
        return null;
    }

    @Override
    public void onRecvQcCode(ByteBuf bf) {

    }

    @Override
    public void onPaid(long orderId) {

    }
}
