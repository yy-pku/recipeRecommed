package com.sun.dbm;

/**
 * 这个库是用来定义对数据库的初始化和关闭的操作
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dbmanage {
    /*
    连接池要在静态代码块里生成,只在类创建的时候加载一次,所有对象共用
     */
    static ConnectionParam connectionParam=new ConnectionParam();
    static ConnectionPool connectionPool=new ConnectionPool();
    static{
        connectionParam.setDriver("com.mysql.jdbc.Driver");
        connectionParam.setUrl("jdbc:mysql://localhost:3306/recipe?useUnicode=true&characterEncoding=utf8");
        connectionParam.setUser("root");
        connectionParam.setPassword("root");
        connectionParam.setMinConnection(5);
        connectionParam.setMaxConnection(500);
        connectionParam.setTimeoutValue(60000);
        connectionParam.setWaitTime(500);
        connectionPool.setParam(connectionParam);
        try {
            connectionPool.createPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Connection initDB(){
        Connection con= null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
//    public Connection initDB() {
//        // 初始化数据库连接方法
//        Connection conn = null;
//        // 创建一个Connection句柄
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            // 加载数据库驱动
//            String url = "jdbc:mysql://localhost/recipe";
//            // 定义数据库地址url，并设置编码格式
//            conn = DriverManager.getConnection(url, "root", "root");
//            // 得到数据连接
//        } catch (ClassNotFoundException e) {
//
//            e.printStackTrace();
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
//        return conn;
//        // 返回数据库连接
//    }


    public void closeDB(Statement sta, Connection conn) {
        // 关闭数据库连接（无结果集）
        try {
            sta.close();
            connectionPool.returnConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDB(ResultSet rs, Statement sta, Connection conn) {
        // 关闭数据库连接（有结果集）
        try {
            rs.close();
            sta.close();
            connectionPool.returnConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    public void closeDB(Statement sta, Connection conn) {
//        // 关闭数据库连接（无结果集）
//        try {
//            sta.close();
//            conn.close();
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
//    }
//
//    public void closeDB(ResultSet rs, Statement sta, Connection conn) {
//        // 关闭数据库连接（有结果集）
//        try {
//            rs.close();
//            sta.close();
//            conn.close();
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
//
//    }
}
