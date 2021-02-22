package com.qingmeng.dao.user;

import com.qingmeng.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {

    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改当前用户密码
    public int updatePwdById(Connection connection, int id, String pwd) throws SQLException;
}
