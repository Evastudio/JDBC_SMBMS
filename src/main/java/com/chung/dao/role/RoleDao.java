package com.chung.dao.role;

import com.chung.pojo.Role;

import java.sql.Connection;
import java.util.List;

/**
 * @author chungstart
 * @create 2020-11-26 15:51
 */
public interface RoleDao {
    //获取角色列表
    public List<Role> getRoleList(Connection connection)throws Exception;
}
