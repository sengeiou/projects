package com.normal.biz.order;

import com.normal.core.web.Result;
import io.netty.buffer.ByteBuf;

public interface IOrderService {

    Result createOrder(Order order);

}
