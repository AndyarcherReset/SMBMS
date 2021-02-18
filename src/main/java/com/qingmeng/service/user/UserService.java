package com.qingmeng.service.user;

import com.qingmeng.pojo.User;

public interface UserService {
    public User login(String userCode, String password);
}
