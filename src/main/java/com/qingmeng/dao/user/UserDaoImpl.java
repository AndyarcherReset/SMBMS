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
import java.util.Date;
import java.util.List;

public class UserDaoImpl implements UserDao {
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        String sql = "select * from smbms_user where userCode=?";
        Object[] params = {userCode};
        return getUserBySql(connection, sql, params);
    }

    private User getUserBySql(Connection connection, String sql, Object[] params) throws SQLException {
        User res = null;

        if (connection != null) {
            PreparedStatement pstm =null;
            ResultSet rs = null;
            rs = BaseDao.execute(connection, pstm, rs, sql, params);

            if (rs.next()) {
                res = new User();
                res.setId(rs.getInt("id"));
                res.setUserCode(rs.getString("userCode"));
                res.setUserName(rs.getString("userName"));
                res.setUserPassword(rs.getString("userPassword"));
                res.setGender(rs.getInt("gender"));
                res.setBirthday(rs.getDate("birthday"));
                res.setPhone(rs.getString("phone"));
                res.setAddress(rs.getString("address"));
                res.setUserRole(rs.getInt("userRole"));
                res.setCreatedBy(rs.getInt("createdBy"));
                res.setCreationDate(rs.getTimestamp("creationDate"));
                res.setModifyBy(rs.getInt("modifyBy"));
                res.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResource(null, pstm, rs);
        }

        return res;
    }
    @Override
    public User queryUserById(Connection connection, int id) throws SQLException {
        String sql = "select * from smbms_user where id=?";
        Object[] params = {id};
        return getUserBySql(connection, sql, params);
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

    @Override
    public int deleteUserById(Connection connection, int id) throws SQLException {
        int res = 0;
        if (connection != null) {
            PreparedStatement pstm = null;
            String sql = "delete from smbms_user where id = ?";
            Object[] params = {id};
            res = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return  res;
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

    @Override
    public int addUser(Connection connection, User user) throws SQLException {
        int res = 0;
        if (connection != null) {
            PreparedStatement pstm = null;

            String sql = "insert into smbms_user (userCode,userName,userPassword,gender,birthday,phone,address,userRole) values "
                    + "(?,?,?,?,?,?,?,?);";
            String userCode = user.getUserCode();
            String userName = user.getUserName();
            String userPassword = user.getUserPassword();
            int gender = user.getGender();
            Date birthday = user.getBirthday();
            String phone = user.getPhone();
            String address = user.getAddress();
            int role = user.getUserRole();
            Object[] params = {userCode,userName, userPassword, gender, birthday, phone, address, role};
            res = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return res;
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
