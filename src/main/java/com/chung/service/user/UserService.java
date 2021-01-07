package com.chung.service.user;

import com.chung.pojo.User;

import java.util.List;

/**
 * @author chungstart
 * @create 2020-11-26 2:45
 */
public interface UserService {
    //用户登录
    public User login(String userCode,String password);
    //根据用户ID修改密码
    public boolean updatePwd(int id,String pwd);
    //查询记录数
    public int getUserCount(String username,int userRole);
    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);
    //根据userCode查询出User
    public User selectUserCodeExist(String userCode);
    //根据ID删除user
    public boolean deleteUserById(Integer delId);
    //根据ID查找user
    public User getUserById(String id);
    //修改用户信息
    public boolean modify(User user);
    //增加用户信息
    public boolean add(User user);
}
