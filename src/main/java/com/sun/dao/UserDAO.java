package com.sun.dao;

import com.sun.dbm.Dbmanage;
import com.sun.vo.UserVo;

import java.sql.*;

/**
 * 这个类操作数据库跟用户表有关的部分
 */
public class UserDAO {
    public void insertUser(UserVo user) {
        // 用户注册方法
        Dbmanage dbmanage = new Dbmanage();
        Connection conn = null;
        Statement sta = null;

        try {
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            System.out.println("Created statement...");
            String sql = "INSERT INTO userTable (user_name,user_password)VALUES('"
                    + user.getUserName()
                    + "','"
                    + user.getUserPassword()
                    + "')";
            sta.executeUpdate(sql);
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            // 执行完关闭数据库
            dbmanage.closeDB(sta, conn);
        }
    }
    public UserVo judgeUserPassword(String userName, String userPassword) {
        // 用户登录验证
        Dbmanage dbmanage = new Dbmanage();
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        UserVo user = null;
        try {
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            System.out.println(userName);
            System.out.println(userPassword);
            String sql = "SELECT * FROM userTable WHERE user_name = '"
                    + userName + "' AND user_password= '" + userPassword + "'";
            rs = sta.executeQuery(sql);
            while (rs.next()) {
                user = new UserVo();
                user.setUserId(rs.getInt("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setUserPassword(rs.getString("user_password"));

            }

        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            // 执行完关闭数据库
            dbmanage.closeDB(rs, sta, conn);
        }
        // 返回查询结果
        return user;
    }
}
