package com.sun.vo;

import java.util.ArrayList;

/**
 * 这个类记录已经登陆的用户,并生成静态类型的动态数组,里面是用户列表
 */
public class UserListVector {
    private static ArrayList<UserVo> list=new ArrayList<UserVo>();

    public static void addUser(UserVo user){
        list.add(user);
    }

    public static ArrayList<UserVo> getList() {
        return list;
    }
}
