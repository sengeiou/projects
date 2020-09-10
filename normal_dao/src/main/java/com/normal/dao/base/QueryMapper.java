package com.normal.dao.base;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
@Mapper
public interface QueryMapper {

    @SelectProvider(type = QuerySqlProvider.class, method = "sql")
    List<Map<String, Object>> query(@Param("querySql") QuerySql querySql);

}
