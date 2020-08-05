package com.normal.biz.order;

import com.normal.bizmodel.Order;

public interface ClientListener {

    void onPaid(Order order);

    void onPayTimeout(Order order);


}
