package com.qingmeng.service.user;

import com.qingmeng.pojo.Role;
import com.qingmeng.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public User login(String userCode, String password);

    //修改当前用户密码
    public boolean updatePwdById(int id, String pwd) ;

    public int queryUserCount(String userName, int userRole);

    public List<User> getUserList( String userName, int userRole, int curPageNo, int pageSize) ;

    public boolean queryUserByUserCode(String userCode);

    public int addUser(User user);
}
