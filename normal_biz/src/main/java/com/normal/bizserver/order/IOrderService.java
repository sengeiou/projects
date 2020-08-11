package com.normal.bizserver.order;

import com.normal.bizmodel.Order;
import com.normal.core.web.Result;

/**
 * @author fei.he
 */
public interface IOrderService {

    Result createOrder(Order order);

    Result queryOrder(long id);

}
