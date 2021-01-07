package com.chung.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.chung.pojo.Role;
import com.chung.pojo.User;
import com.chung.service.role.RoleService;
import com.chung.service.role.RoleServiceImpl;
import com.chung.service.user.UserService;
import com.chung.service.user.UserServiceImpl;
import com.chung.util.Constants;
import com.chung.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author chungstart
 * @create 2020-11-26 12:19
 */
//实现Servlet复用
public class UserServlet extends HttpServlet {
    @Override

protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String method = req.getParameter("method");
    if (method.equals("savepwd")&&method!=null){
        this.updatePwd(req,resp);
    }else if (method.equals("pwdmodify")&&method!=null){
        this.pwdModify(req,resp);
    }else if (method.equals("query")&&method!=null){
        this.query(req,resp);
    }else if (method.equals("add")&&method!=null){
        this.add(req,resp);
    }else if (method.equals("view")&&method!=null){
        this.getUserById(req,resp,"userview.jsp");
    }else if(method.equals("ucexist")&&method != null) {
        this.userCodeExist(req,resp);
    }else if (method.equals("deluser")&&method!=null){
        this.delUser(req,resp);
    }else if (method.equals("getrolelist")&&method!=null){
        this.getRoleList(req,resp);
    }else if (method.equals("modifyexe")&&method!=null){
        this.modify(req,resp);
    }else if (method.equals("modify")&&method!=null){
        this.getUserById(req,resp,"usermodify.jsp");
    }
    /*if ( method != null && method != "savepwd") {
        this.updatePwd(req, resp);
    }else if (method.equals("pwdmodify") && method != null) {
        this.pwdModify(req, resp);
    }else if (method.equals("query") && method!=null) {
        this.query(req, resp);
    }*/
    //实现复用~~~~~~
    // 想添加新的增删改查，直接用if(method != "savepwd" && method != null);
}

    private void delUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            UserService userService = new UserServiceImpl();
            if(userService.deleteUserById(delId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        //把resultMap转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }


    private void userCodeExist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //判断用户账号是否可用
        String userCode = request.getParameter("userCode");

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        }else{
            UserService userService = new UserServiceImpl();
            User user = userService.selectUserCodeExist(userCode);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }

        //把resultMap转为json字符串以json的形式输出
        //配置上下文的输出类型
        response.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = response.getWriter();
        //把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }
    private void modify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        if(userService.modify(user)){
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else{
            request.getRequestDispatcher("usermodify.jsp").forward(request, response);
        }

    }
    private void getRoleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }
    private void getUserById(HttpServletRequest request, HttpServletResponse response,String url)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //重点难点
    public void query(HttpServletRequest req, HttpServletResponse resp){
        //查询用户列表
        //从前端获取数据
        //查询用户列表
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = null;

        //第一此请求肯定是走第一页，页面大小固定的
        //设置页面容量
        int pageSize = 5;//建议把它设置在配置文件里,后面方便修改
        //当前页码
        int currentPageNo = 1;

        if(queryUserName == null){
            queryUserName = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);//给查询赋值 0,1,2,3
        }
        if(pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获取用户总数（分页	上一页：下一页的情况）
        //总数量（表）,总共有几页
        int totalCount	= userService.getUserCount(queryUserName,queryUserRole);

        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        //控制首页和尾页
        int totalPageCount =pageSupport.getTotalPageCount();//总共有几页
        //(totalCount+pageSize-1/pageSize)取整
        // pageSupport.getTotalCount()

        //System.out.println("totalCount ="+totalCount);
        //System.out.println("pageSize ="+pageSize);
        //System.out.println("totalPageCount ="+totalPageCount);

        //如果页面小于 1 就显示第一页的东西
        if(currentPageNo < 1) {
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount) {//如果页面大于了最后一页就显示最后一页
            currentPageNo =totalPageCount;
        }

        //获取用户列表展示
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
        // 通过session获得用户id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");

        boolean flag = false;
        if (o != null && newpassword != null && newpassword.length() != 0 ) {
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            if (flag) {
                req.setAttribute("message", "密码修改成功，请退出，使用新密码登录");
                // 密码修改成功,移除session(移除后不能再次修改密码,建议不移除)
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                // 密码修改失败
                req.setAttribute("message", "密码修改失败");
            }

        } else {
            // 密码修改有问题
            req.setAttribute("message", "新密码有问题");
        }
        try {
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //验证旧密码,Session中有用户的旧密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        // 通过session获得用户id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        //万能的Map：结果集
        Map<String, String> resultMap = new HashMap<String,String>();

        if(o==null) {//session失效，session过期了
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){//输入密码为空
            resultMap.put("result","error");
        }else {//
            String userPassword = ((User) o).getUserPassword();//seesion中的用户密码
            if(oldpassword.equals(userPassword)) {
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            /*
             * resultMap = ["result","sessionerror","result",error]
             * josn格式={key,value}
             */
            //writer.write(JSONArray.toJSONString(resultMap));
            //JSONArray阿里巴巴的JSON工具类,转换格式
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //增加用户
    private void add(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("add()================");
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserServiceImpl();
        if(userService.add(user)){
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else{
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }

    }

}
