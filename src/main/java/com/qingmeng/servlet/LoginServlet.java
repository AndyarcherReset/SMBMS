package com.qingmeng.servlet;

import com.qingmeng.pojo.User;
import com.qingmeng.service.user.UserServiceImpl;
import com.qingmeng.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        //调用业务层
        UserServiceImpl userService = new UserServiceImpl();
        User login = userService.login(userCode, userPassword);

        //登录成功
        if (login != null) {
            //存储session
            req.getSession().setAttribute(Constants.USER_SESSION,login);
            //跳转界面
            resp.sendRedirect("jsp/frame.jsp");
        }else {
            //无法登录，转发回登录界面，顺便提示错误
            req.setAttribute("error","用户名或者密码不正确！");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
