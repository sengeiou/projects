package com.normal.resources.impl;

import com.normal.order.IOrderService;
import com.normal.model.order.Order;
import com.normal.model.order.OrderStatus;
import com.normal.base.web.CommonErrorMsg;
import com.normal.base.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fei.he
 */
@Service
public class ResourceService {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ResourceDao resourceDao;

    public Result queryRealUrl(long ordId) {
        Result result = orderService.queryOrder(ordId);
        Order order = (Order) result.getData();
        if (order != null && order.getOrderStatus().equals(OrderStatus.PAIED)) {
            Resource resource = resourceDao.queryByPrimaryKey(order.getReferId());
            return Result.success(resource.getRealUrl());
        }
        return Result.fail(CommonErrorMsg.ILLEGE_ARG.getCode(), "not paid yet");
    }
}
