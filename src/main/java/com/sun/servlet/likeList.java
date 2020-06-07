package com.sun.servlet;

import com.sun.dao.DisDAO;
import com.sun.dao.LikeDAO;
import com.sun.dao.SimDAO;
import com.sun.rec_engin.similarity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 这个servlet用来返回,用户已收藏的菜谱的列表(get方法
 * 同时可以实现删除功能(post方法
 */
@WebServlet(name = "likeList")
public class likeList extends HttpServlet {
    LikeDAO likedao=new LikeDAO();
    similarity sim=new similarity();
    DisDAO disdao=new DisDAO();
    SimDAO simdao=new SimDAO();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] list=request.getParameterValues("list");
        int id=Integer.parseInt(request.getParameter("id"));
        System.out.println(id);
        ArrayList<Integer> recipeid=new ArrayList<Integer>();
        for (String s:list) {
            System.out.println(s);
            recipeid.add(Integer.parseInt(s));
        }
        likedao.deleteLike(id,recipeid);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id=Integer.parseInt(request.getParameter("id"));
        ArrayList<Integer> arr=new ArrayList<Integer>();
        arr=likedao.queryLike(id);
        request.setAttribute("list", arr);
        request.setAttribute("id",id);
        request.getRequestDispatcher("/like_list.jsp").forward(request, response);
    }
}
