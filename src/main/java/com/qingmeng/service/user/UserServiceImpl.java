package com.qingmeng.service.user;

import com.qingmeng.dao.BaseDao;
import com.qingmeng.dao.user.UserDao;
import com.qingmeng.dao.user.UserDaoImpl;
import com.qingmeng.pojo.User;

import java.awt.geom.FlatteningPathIterator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    @Override
    public int deleteUserById(int id) {
        int res = 0;
        Connection connection = BaseDao.getConnection();
        try {
            connection.setAutoCommit(false);
            res = userDao.deleteUserById(connection, id);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(connection, null, null);
        }

        return res;
    }

    @Override
    public int addUser(User user) {
        int res = 0;
        Connection connection = BaseDao.getConnection();
        if (user != null) {
            try {
                connection.setAutoCommit(false);
                res = userDao.addUser(connection, user);
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } finally {
                BaseDao.closeResource(connection, null, null);
            }
        }
        return res;
    }

    @Override
    public User queryUserById(int id) {
        User user = null;
        Connection connection = BaseDao.getConnection();
        try {
            //业务层调用对应的数据库
            user = userDao.queryUserById(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }

    @Override
    public boolean queryUserByUserCode(String userCode) {
        boolean res = false;
        Connection connection = BaseDao.getConnection();
        User user = null;

        try {
            //业务层调用对应的数据库
            user = userDao.getLoginUser(connection, userCode);
            if (user != null) {
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return res;
    }

    public User login(String userCode, String password) {
        Connection connection = BaseDao.getConnection();
        User user = null;

        try {
            //业务层调用对应的数据库
            user = userDao.getLoginUser(connection, userCode);
            if (user != null) {
                if (!password.equals(user.getUserPassword())) {
                    user = null;
                }
            }else {
                user = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }

    public boolean updatePwdById(int id, String pwd) {
        Connection connection = BaseDao.getConnection();
        boolean res = false;

        try {
            //业务层调用对应的数据库
            if(userDao.updatePwdById(connection, id, pwd)>0){
                res =true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return res;
    }

    public List<User> getUserList(String userName, int userRole, int curPageNo, int pageSize) {
        Connection connection = BaseDao.getConnection();
        List<User> res = new ArrayList<User>();

        try {
            //业务层调用对应的数据库
            res = userDao.getUserList(connection, userName, userRole, curPageNo, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return res;
    }

    public int queryUserCount(String userName, int userRole) {
        Connection connection = BaseDao.getConnection();
        int res = 0;

        try {
            //业务层调用对应的数据库
            res = userDao.queryUserCount(connection, userName, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return res;
    }
}
