package com.normal.biz.order;

import com.normal.bizmodel.Order;
import com.normal.core.web.Result;

public interface IOrderService {

    Result createOrder(Order order);

    Result queryOrder(long id);

}
