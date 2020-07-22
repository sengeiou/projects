package com.normal.biz.order;

public interface ClientListener {

    void onPaid(Order order);

    void onPayTimeout(Order order);


}
