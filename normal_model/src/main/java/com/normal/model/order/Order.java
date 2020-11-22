package com.normal.model.order;



import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fei.he
 */

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReferId() {
        return referId;
    }

    public void setReferId(long referId) {
        this.referId = referId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getValidDateTime() {
        return validDateTime;
    }

    public void setValidDateTime(LocalDateTime validDateTime) {
        this.validDateTime = validDateTime;
    }
}
