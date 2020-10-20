package com.normal.user;

import com.normal.base.utils.Digests;
import com.normal.base.web.CommonErrorMsg;
import com.normal.base.web.Result;
import com.normal.dao.user.UserMapper;
import com.normal.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: fei.he
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Result register(User user) {
        User existed = userMapper.selectByPhone(user.getPhone());
        if (existed != null) {
            return Result.fail("用户已注册");
        }
        user.setPwd(Digests.md5(user.getPwd()));
        userMapper.insertSelective(user);
        String token = genToken(user);
        return Result.success(token);
    }

    private String genToken(User user) {
        return null;
    }

    @Override
    public Result login(User user) {
        User existed = userMapper.selectByPhone(user.getPhone());
        if (existed == null) {
            return Result.fail("无此用户");
        }
        if (existed.getPwd().equals(user.getPwd())) {
            return Result.success(genToken(user));
        }
        return Result.fail("密码错误");
    }


    @Override
    public Result modifyPwd(String newPwd, String verifyCode) {
        return null;
    }

    @Override
    public Result queryUser(Integer userId) {
        return Result.success(userMapper.selectByPrimaryKey(userId));
    }
}
