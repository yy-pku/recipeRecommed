package com.sun.dao;

import com.sun.dbm.Dbmanage;
import com.sun.vo.RecipeVo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 这个类用于操作对数据库的recipe进行显示
 */
public class DisDAO {//从数据库中选择recipe显示

    public static ArrayList idlist=new ArrayList();
    static {//直接生成的静态代码块
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        Dbmanage dbmanage = new Dbmanage();

        try {
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            System.out.println("Created statement...");
            String sql = "SELECT `recipe`.`菜谱ID` FROM `recipe`";
            rs = sta.executeQuery(sql);
            while (rs.next()){
                int z=rs.getInt("菜谱ID");
                idlist.add(z);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 执行完关闭数据库
            dbmanage.closeDB(rs, sta, conn);
        }
    }
    int disNum=20;//每页显示的数目
    int totalPage=idlist.size()/disNum+1;
    public int gettotalPage(){
        return totalPage;
    }
    public RecipeVo createRes(ResultSet rs){
        RecipeVo recipe=null;
        try {
            while (rs.next()){
                recipe=new RecipeVo();
                recipe.setId(rs.getInt("菜谱ID"));
                recipe.setName(rs.getString("菜谱名称"));
                recipe.setZuofa(rs.getString("做法"));
                recipe.setTexing(rs.getString("特性"));
                recipe.setTishi(rs.getString("提示"));
                recipe.setTiaoliao(rs.getString("调料"));
                recipe.setYuanliao(rs.getString("原料"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public ArrayList<RecipeVo> display(int pageNum){//返回动态数组,里面都是RecipeVo
        int page=pageNum;//分页显示

        Dbmanage dbmanage = new Dbmanage();
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        ArrayList<RecipeVo> rvl=new ArrayList<RecipeVo>();

        int start=(page-1)*disNum;

        try {
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            for(int i=start;i<start+disNum;i++){
                if(i>=idlist.size()){
                    break;
                }
                String sql="SELECT * FROM `recipe` WHERE `recipe`.`菜谱ID`="+idlist.get(i);
                rs = sta.executeQuery(sql);
                rvl.add(createRes(rs));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            // 执行完关闭数据库
            dbmanage.closeDB(rs, sta, conn);
        }
        return rvl;
    }


    public ArrayList<RecipeVo> display(int[] displaylist){//方法不同显示的不同,这次是按照给定的列表显示
        int[] dislist=displaylist;

        ArrayList<RecipeVo> rvl=new ArrayList<RecipeVo>();
        Dbmanage dbmanage = new Dbmanage();
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        try {
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            System.out.println("Created statement...");
            for(int i=0;i<dislist.length;i++){
                if(i>=dislist.length){
                    break;
                }
                String sql="SELECT * FROM `recipe` WHERE `recipe`.`菜谱ID`="+dislist[i];
                rs = sta.executeQuery(sql);
                rvl.add(createRes(rs));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            // 执行完关闭数据库
            dbmanage.closeDB(rs, sta, conn);
        }
        return rvl;
    }
    public ArrayList<RecipeVo> display(ArrayList<Integer> displaylist){//方法不同显示的不同,这次是按照给定的列表显示

        ArrayList<RecipeVo> rvl=new ArrayList<RecipeVo>();
        Dbmanage dbmanage = new Dbmanage();
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        try {
            conn = dbmanage.initDB();
            sta = conn.createStatement();
            System.out.println("Created statement...");
            for(int i=0;i<displaylist.size();i++){
                String sql="SELECT * FROM `recipe` WHERE `recipe`.`菜谱ID`="+displaylist.get(i);
                rs = sta.executeQuery(sql);
                rvl.add(createRes(rs));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            // 执行完关闭数据库
            dbmanage.closeDB(rs, sta, conn);
        }
        return rvl;
    }
}
