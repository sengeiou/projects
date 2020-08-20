package com.normal.order;

import com.normal.model.order.Order;
import com.normal.base.web.Result;

/**
 * @author fei.he
 */
public interface IOrderService {

    Result createOrder(Order order);

    Result queryOrder(long id);

}
