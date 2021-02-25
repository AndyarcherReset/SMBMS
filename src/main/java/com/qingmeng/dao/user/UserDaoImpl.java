package com.qingmeng.dao.user;

import com.mysql.jdbc.StringUtils;
import com.qingmeng.dao.BaseDao;
import com.qingmeng.pojo.Role;
import com.qingmeng.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;

        if (connection != null) {
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};

            rs = BaseDao.execute(connection, pstm, rs, sql, params);

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return user;
    }

    //修改用户密码
    public int updatePwdById(Connection connection, int id, String pwd) throws SQLException {
        int res=0;
        if (connection != null) {
            PreparedStatement pstm = null;
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object[] params = {pwd, id};
            res = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return res;
    }

    public int queryUserCount(Connection connection, String userName, int userRole) throws SQLException {
        int count = 0;

        if (connection != null) {
            PreparedStatement pstm = null;
            ResultSet rs = null;
            StringBuffer sql = new StringBuffer();
            sql.append("select count(0) as count from smbms_user u, smbms_role R WHERE U.userRole=R.id");

            ArrayList<Object> objects = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                objects.add("%"+userName+"%");
            }

            if (userRole>0) {
                sql.append(" and u.userRole = ?");
                objects.add(userRole);
            }
            Object[] params = objects.toArray();
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);

            if (rs.next()) {
                count = rs.getInt("count");
            }

            BaseDao.closeResource(null, pstm, rs);
        }
        return count;
    }

    public List<User> getUserList(Connection connection, String userName, int userRole, int curPageNo, int pageSize) throws SQLException {
        List<User> res = new ArrayList<User>();

        if (connection != null) {
            PreparedStatement pstm = null;
            ResultSet rs = null;
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*, r.roleName as userRoleName from smbms_user u, smbms_role r WHERE u.userRole=r.id");

            ArrayList<Object> objects = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                objects.add("%"+userName+"%");
            }

            if (userRole>0) {
                sql.append(" and u.userRole = ?");
                objects.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,?");
            curPageNo = (curPageNo-1)*pageSize;
            objects.add(curPageNo);
            objects.add(pageSize);

            Object[] params = objects.toArray();
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                //user.setuserr(rs.getInt("userRole"));
                res.add(user);
            }

            BaseDao.closeResource(null, pstm, rs);
        }
        return res;
    }
}
