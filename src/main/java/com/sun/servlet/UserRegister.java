package com.sun.servlet;

import com.sun.dao.UserDAO;
import com.sun.vo.UserVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用户注册的servlet
 */
public class UserRegister extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        // 设置编码格式为 UTF-8
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        // 前台得到 用户输入数据

        UserVo user = new UserVo();
        // 实例化一个VO对象
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        // 将前台得到的数据存入ＶＯ
        UserDAO userDao = new UserDAO();
        // 实例化一个数据库操作对象
        userDao.insertUser(user);
        // 调用增加用户方法
        request.getRequestDispatcher("/userlogin.jsp").forward(request,
                response);
        // 转到登录页面
    }
}
