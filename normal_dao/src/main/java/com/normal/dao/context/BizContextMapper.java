package com.normal.dao.context;

import com.normal.model.context.BizContext;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: fei.he
 */
@Mapper
public interface BizContextMapper {

    void insert(BizContext context);

    BizContext queryByTypeKey(String id);

    void deleteByType(String id);

    void updateCtxObjByType(@Param("typeKey") String typeKey, @Param("ctxJson")String ctxJson);
}
