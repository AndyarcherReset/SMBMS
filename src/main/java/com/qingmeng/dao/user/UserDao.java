package com.qingmeng.dao.user;

import com.qingmeng.pojo.Role;
import com.qingmeng.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改当前用户密码
    public int updatePwdById(Connection connection, int id, String pwd) throws SQLException;

    //查询用户总数
    public int queryUserCount(Connection connection, String userName, int userRole) throws SQLException;

    //获取用户列表
    public List<User> getUserList(Connection connection, String userName,int userRole, int curPageNo, int pageSize)throws SQLException;

    //添加用户
    public int addUser(Connection connection, User user) throws SQLException;

    //删除用户
    public int deleteUserById(Connection connection, int id) throws SQLException;

    User queryUserById(Connection connection, int id) throws SQLException;
}
