package com.normal.biz.order;

import com.normal.bizmodel.Order;

/**
 * @author fei.he
 */
public interface ClientListener {

    void onPaid(Order order);

    void onPayTimeout(Order order);


}
