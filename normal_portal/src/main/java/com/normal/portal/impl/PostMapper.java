package com.normal.portal.impl;

import com.normal.base.mybatis.PageParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPrimaryKey(Integer id);

    List<Post> selectByPage(PageParam param);

    Post selectByFileName(String fileName);
}