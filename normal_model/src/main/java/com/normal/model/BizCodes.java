package com.normal.model;

/**
 * @author: fei.he
 */
public  interface BizCodes {
    /**
     * 订单模块使用常量
     */
    String ORDER_PAID_FINISHED = "ORDER_PAID_FINISHED";
    String ORDER_PAID_TIMEOUT = "ORDER_PAID_TIMEOUT";
    String ORDER_QUERY_ALIPAY_ORDERS = "ORDER_QUERY_ALIPAY_ORDERS";
    String ORDER_STATUS_RECV = "ORDER_STATUS_RECV";

    /**
     * 自动发送模块使用常量
     */
    String AUTO_SEND_NEED_PWD= "AUTO_SEND_NEED_PWD";

    /**
     * open api 常量
     */
    String OPEN_API_ERRORCODE_50001= "50001";

    String CATS_CACHE_KEY = "key$1";
}
