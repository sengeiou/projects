package com.normal.resources.impl;

import com.normal.core.mybatis.PageParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(Resource record);

    Resource queryByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Resource record);

    List<Resource> queryResources(PageParam page);


}