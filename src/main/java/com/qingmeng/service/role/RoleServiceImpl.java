package com.qingmeng.service.role;

import com.qingmeng.dao.BaseDao;
import com.qingmeng.dao.role.RoleDao;
import com.qingmeng.dao.role.RoleDaoImpl;
import com.qingmeng.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;

    public RoleServiceImpl(){
        this.roleDao = new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        Connection connection = BaseDao.getConnection();
        List<Role> res = new ArrayList<Role>();
        try {
            //业务层调用对应的数据库
            res = roleDao.getRoleList(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return res;
    }
}
