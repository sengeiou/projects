package com.normal.bizassistant.autosend;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: fei.he
 */
@Mapper
public interface SendGoodMapper {

    void batchInsert(List<SendGood> goods);

}
