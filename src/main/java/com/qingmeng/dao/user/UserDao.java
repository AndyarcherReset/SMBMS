package com.qingmeng.dao.user;

import com.qingmeng.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {

    public User getLoginUser(Connection connection, String userCode) throws SQLException;
}
