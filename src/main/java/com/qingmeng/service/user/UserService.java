package com.qingmeng.service.user;

import com.qingmeng.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserService {
    public User login(String userCode, String password);

    //修改当前用户密码
    public boolean updatePwdById(int id, String pwd) ;
}
