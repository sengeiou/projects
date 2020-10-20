package com.normal.user;

import com.normal.base.web.Result;
import com.normal.model.user.User;

/**
 * @author: fei.he
 */
public interface UserService {

    Result register(User user);

    Result login(User user);

    Result modifyPwd(String newPwd, String verifyCode);

    Result queryUser(Integer userId);


}
