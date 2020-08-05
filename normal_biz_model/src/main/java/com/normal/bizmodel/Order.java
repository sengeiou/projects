package com.normal.bizmodel;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fei.he
 */
@Data
public class Order {

    private long id;

    private long referId;

    private BigDecimal price;

    private Integer num = 1;

    private OrderStatus orderStatus = OrderStatus.NEW;

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    private String remark;

    private LocalDateTime validDateTime;
}
