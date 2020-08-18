package com.normal.bizassistant;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author: fei.he
 */
@Mapper
public interface BizContextMapper {

    void insert(BizContext context);

    BizContext queryByType(String id);

    void deleteByType(String id);
}
