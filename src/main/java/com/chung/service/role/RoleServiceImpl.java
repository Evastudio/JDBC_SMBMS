package com.chung.service.role;

import com.chung.dao.BaseDao;
import com.chung.dao.role.RoleDao;
import com.chung.dao.role.RoleDaoImpl;
import com.chung.pojo.Role;

import java.sql.Connection;
import java.util.List;

/**
 * @author chungstart
 * @create 2020-11-26 15:55
 */
public class RoleServiceImpl implements RoleService{

    //引入Dao
    private RoleDao roleDao;

    public RoleServiceImpl(){
        roleDao = new RoleDaoImpl();
    }
    @Override
    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;
        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return roleList;
    }
}
