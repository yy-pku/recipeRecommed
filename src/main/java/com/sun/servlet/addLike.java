package com.sun.servlet;

import com.sun.dao.LikeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 给收藏数据库添加(用户,菜谱id)
 */
@WebServlet(name = "addLike")
public class addLike extends HttpServlet {
    LikeDAO likedao=new LikeDAO();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] list=request.getParameterValues("list");
        int id=Integer.parseInt(request.getParameter("id"));
        System.out.println(id);
        ArrayList<Integer> recipeid=new ArrayList<Integer>();
        for (String s:list) {
            System.out.println(s);
            recipeid.add(Integer.parseInt(s));
        }
        likedao.insertLike(id,recipeid);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
