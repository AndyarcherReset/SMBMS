package com.qingmeng.service.user;

import com.qingmeng.dao.BaseDao;
import com.qingmeng.dao.user.UserDao;
import com.qingmeng.dao.user.UserDaoImpl;
import com.qingmeng.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService{

    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User login(String userCode, String password) {
        Connection connection = BaseDao.getConnection();
        User user = null;

        try {
            //业务层调用对应的数据库
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }
}
