package com.sun.servlet;

import com.sun.dao.UserDAO;
import com.sun.vo.UserListVector;
import com.sun.vo.UserVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登陆的servlet
 */
public class Userlogin extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        // 前台得到用户输入
        UserDAO userDao = new UserDAO();
        UserVo user = userDao.judgeUserPassword(userName, userPassword);
        // 调用方法判断用户是否存在
        String message = "用户名或密码错误！";
        if (user == null) {
            // 如果用户不存在，重新登录
            request.setAttribute("message", message);
            request.getRequestDispatcher("/userlogin.jsp").forward(request,
                    response);

        } else {
            // 如果用户存在，检索数据，跳到菜谱列表显示页面
            UserListVector ul=new UserListVector();//每登陆一个用户就添加一个
            ul.addUser(user);
            String id=String.valueOf(user.getUserId());
            request.getRequestDispatcher("/rec_sys.jsp?page=1&id="+id).forward(request,
                    response);
        }

    }
}
