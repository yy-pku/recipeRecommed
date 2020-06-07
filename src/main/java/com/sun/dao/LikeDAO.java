package com.sun.dao;

import com.sun.dbm.Dbmanage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 这个类用于和servlet的likeList配合,将用户标记喜欢的菜谱存入liketable
 */
public class LikeDAO {
    public ArrayList<Integer> queryLike(int userid){//返回用户喜欢的菜谱id可变数组
        Dbmanage dbmanage = new Dbmanage();
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        ArrayList<Integer> arr=new ArrayList<Integer>();
        try {
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            System.out.println("Created statement...");
            String sql = "SELECT `liketable`.`recipeid` FROM `liketable` WHERE `liketable`.`user_id`="+userid;
            rs=sta.executeQuery(sql);
            while (rs.next()){
                arr.add(rs.getInt("recipeid"));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            // 执行完关闭数据库
            dbmanage.closeDB(sta, conn);
        }
        return arr;
    }
    public void insertLike(int userid,ArrayList<Integer> recipeid){//把用户标记喜欢的菜谱加入数据库
        Dbmanage dbmanage = new Dbmanage();
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;

        try {
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            System.out.println("Created statement...");
            String sql = "SELECT `liketable`.`recipeid` FROM `liketable` WHERE `liketable`.`user_id`="+userid;
            rs=sta.executeQuery(sql);
            while (rs.next()){
                /*
                这个是通过迭代器把recipeid里面的重复元素去掉.
                 */
                Iterator<Integer> sListIterator = recipeid.iterator();
                while(sListIterator.hasNext()){
                    int e = sListIterator.next();
                    if(e==rs.getInt("recipeid")){
                        sListIterator.remove();
                    }
                }
            }
            /*
            这是把喜欢列表添加进数据库
             */
            Iterator<Integer> sListIterator = recipeid.iterator();
            while(sListIterator.hasNext()){
                int e=sListIterator.next();
                sql="INSERT INTO liketable(user_id,recipeid) VALUES("+userid+","+e+")";
                sta.executeUpdate(sql);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            // 执行完关闭数据库
            dbmanage.closeDB(sta, conn);
        }
    }
    public void deleteLike(int userid,ArrayList<Integer> recipeid){
        Dbmanage dbmanage = new Dbmanage();
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        String sql=null;
//        String sql="DELETE FROM `liketable` WHERE `liketable`.`user_id`=1 AND `liketable`.`like_id`=966";
        try{
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            System.out.println("Created statement...");
            for (int i = 0; i < recipeid.size(); i++) {
                sql="DELETE FROM `liketable` WHERE `liketable`.`user_id`="+userid+" AND `liketable`.`recipeid`="+recipeid.get(i);
                sta.execute(sql);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            // 执行完关闭数据库
            dbmanage.closeDB(sta, conn);
        }
    }
}
