package com.qingmeng.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//数据库公共类
public class BaseDao {
    private static String driver;
    private static String url;
    private static String userName;
    private static String pwd;

    static {

        InputStream in = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();

        try {
            properties.load(in);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            userName = properties.getProperty("username");
            pwd = properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //H获取数据库连接
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    //公共查询
    public static ResultSet execute(Connection connection,PreparedStatement preparedStatement, ResultSet resultSet, String sql, Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1,params[0]);
        }

        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //公共增删改
    public static int execute(Connection connection,PreparedStatement preparedStatement, String sql, Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1,params[0]);
        }

        int udpateRows = preparedStatement.executeUpdate();
        return udpateRows;
    }

    //释放资源
    public static boolean closeResource(Connection connection,PreparedStatement preparedStatement, ResultSet resultSet) {
        boolean flag = true;

        if (resultSet != null) {
            try {
                resultSet.close();
                resultSet=null;
            } catch (SQLException e){
                e.printStackTrace();
                flag=false;
            }
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                preparedStatement=null;
            } catch (SQLException e){
                e.printStackTrace();
                flag=false;
            }
        }

        if (connection != null) {
            try {
                connection.close();
                connection=null;
            } catch (SQLException e){
                e.printStackTrace();
                flag=false;
            }
        }

        return flag;
    }
}
