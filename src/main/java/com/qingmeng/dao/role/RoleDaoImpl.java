package com.qingmeng.dao.role;

import com.qingmeng.dao.BaseDao;
import com.qingmeng.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    public List<Role> getRoleList(Connection connection) throws SQLException {
        List<Role> res = new ArrayList<Role>();
        if (connection != null) {
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String sql = "select * from smbms_role";
            Object[] params = {};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("roleName"));
                role.setRoleCode(rs.getString("roleCode"));
                res.add(role);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return res;
    }

}
