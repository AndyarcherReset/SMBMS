package com.qingmeng.servlet;

import com.alibaba.fastjson.JSONArray;
import com.qingmeng.pojo.User;
import com.qingmeng.service.user.UserService;
import com.qingmeng.service.user.UserServiceImpl;
import com.qingmeng.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ModifyPwdServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        System.out.println(method);
        if("savepwd".equals(method)){
            this.updatePwd(req, resp);
        } else if ("checkPwd".equals(method)) {
            this.checkPwd(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    //更新密码
    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        if(o!=null && !newpassword.isEmpty()){
            UserService userService = new UserServiceImpl();
            if (userService.updatePwdById(((User)o).getId(), newpassword)){
                req.setAttribute("message", "密码修改成功！");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message", "密码修改失败！");
            }
        }
        else {
            req.setAttribute("message", "新密码有问题！");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //验证旧密码，session中存在旧密码
    private void checkPwd(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        System.out.println(oldpassword);
        Map<String, String> resultMap = new HashMap<String, String>();
        if (o == null ) {
            resultMap.put("result", "sessionerror");
        } else if (oldpassword.isEmpty()) {
            resultMap.put("result", "error");
        } else{
            String userPwd = ((User)o).getUserPassword();//session中的密码
            if (oldpassword.equals(userPwd)){
                resultMap.put("result", "true");
            }else {
                resultMap.put("result", "false");
            }
        }

        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
