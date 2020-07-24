package com.normal.biz.order;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateOrderStatus(@Param("id") long id, @Param("orderStatus") OrderStatus orderStatus);

}