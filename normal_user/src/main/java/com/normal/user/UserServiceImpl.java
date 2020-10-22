package com.normal.user;

import com.normal.base.utils.Digests;
import com.normal.base.utils.Tokens;
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
        Tokens tokens = new Tokens();
        tokens.addPayload("phone", user.getPhone());

        return tokens.gen();
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
        //todo
        return null;
    }

    @Override
    public Result queryUser(String token) {
        Tokens tokens = new Tokens();
        String userId = tokens.getPayloadItem(token, "userId");
        return Result.success(userMapper.selectByPrimaryKey(Integer.valueOf(userId)));
    }

    @Override
    public Result updateUser(User user) {
        int count = userMapper.updateByPrimaryKeySelective(user);
        return count == 1 ? Result.success() : Result.fail("更新失败");
    }
}
