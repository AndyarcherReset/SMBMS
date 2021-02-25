package com.qingmeng.servlet;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.qingmeng.pojo.Role;
import com.qingmeng.pojo.User;
import com.qingmeng.service.role.RoleServiceImpl;
import com.qingmeng.service.user.UserService;
import com.qingmeng.service.user.UserServiceImpl;
import com.qingmeng.util.Constants;
import com.qingmeng.util.PageBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifyPwdServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");

        if("savepwd".equals(method)){
            this.updatePwd(req, resp);
        } else if ("checkPwd".equals(method)) {
            this.checkPwd(req, resp);
        } else if ("query".equals(method)) {
            this.query(req, resp);
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //从前端获取数据
        String queryname = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");

        UserServiceImpl userService = new UserServiceImpl();
        int pageSize = 5;
        int curPageNo = 1;

        if (StringUtils.isNullOrEmpty(queryname)) {
            queryname = "";
        }

        int queryUserRole=0;
        if (!StringUtils.isNullOrEmpty(temp)) {
            queryUserRole = Integer.parseInt(temp);
        }

        if (!StringUtils.isNullOrEmpty(pageIndex)) {
            curPageNo = Integer.parseInt(pageIndex);
        }

        int total = userService.queryUserCount(queryname, queryUserRole);
        PageBean pageBean = new PageBean(curPageNo, pageSize, total);

        int totalPage = pageBean.getTotalPage();
        //控制首页和尾页
        if (totalPage < 1) {
            curPageNo = 1;
        } else if (curPageNo > totalPage) {
            curPageNo = totalPage;
        }

        //获取用户列表
        List<User> userList = userService.getUserList(queryname, queryUserRole, curPageNo, pageSize);

        req.setAttribute("userList", userList);

        //获取角色列表
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        //设置页码
        req.setAttribute("totalCount", total);
        req.setAttribute("currentPageNo", curPageNo);
        req.setAttribute("totalPageCount", totalPage);

        req.setAttribute("queryname", queryname);
        req.setAttribute("queryUserRole", queryUserRole);

        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
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
