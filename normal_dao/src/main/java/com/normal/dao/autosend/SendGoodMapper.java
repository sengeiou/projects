package com.normal.dao.autosend;

import com.normal.model.autosend.SendGood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: fei.he
 */
@Mapper
public interface SendGoodMapper {

    void batchInsert(@Param("goods") List<SendGood> goods);

    List<SendGood> queryUnSendGoods();

    void updateSendGoodsStatus(Integer id);
}
