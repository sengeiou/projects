package com.normal.dao.user;

import com.normal.model.user.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectByPhone(String phone);
}