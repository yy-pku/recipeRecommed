package com.sun.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserVo {
    private int userId;
    // 用户Id
    private String userName ;
    // 用户姓名
    private String userPassword;
    // 用户密码
    private static ArrayList<Integer> like=new ArrayList<Integer>();//存放用户喜欢的菜谱的id

    public ArrayList<Integer> getLike() {
        return like;
    }

    public void setLike(ArrayList<Integer> like) {
        this.like = like;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
