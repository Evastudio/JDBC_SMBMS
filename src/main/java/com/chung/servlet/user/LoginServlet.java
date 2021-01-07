package com.chung.servlet.user;

import com.chung.pojo.User;
import com.chung.service.user.UserService;
import com.chung.service.user.UserServiceImpl;
import com.chung.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chungstart
 * @create 2020-11-26 3:01
 */
public class LoginServlet extends HttpServlet {
    //控制层调用业务层代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet--start...");
        //获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        //调用service(业务层)方法，在数据库中进行用户密码匹配
        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);//到此处已经把登录的人查出来了
        if (user != null) {//登录成功
            //将此用户信息放入session
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            //页面跳转（frame.jsp）,跳转到主页
            resp.sendRedirect("jsp/frame.jsp");
        } else {
            //此无此人，转发到登录页面，顺带提醒用户名或密码不正确
            //页面跳转（login.jsp）带出提示信息--转发
            req.setAttribute("error", "用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}



