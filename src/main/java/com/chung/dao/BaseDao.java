package com.chung.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author chungstart
 * @create 2020-11-26 1:41
 */
//数据库操作的公共类,映射着数据库db.properties
public class BaseDao {
    private static final String driver;
    private static final String url;
    private static final String username;
    private static final String password;

    //静态代码块，类加载的时候就初始化了
    static {
        Properties properties = new Properties();
        //通过类加载器读取相应的资源
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("user");
        password = properties.getProperty("password");
    }
    //获取连接数据库
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driver);
            connection= DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    //编写查询公共类
    public static ResultSet execute(Connection connection, PreparedStatement preparedStatement,ResultSet resultSet, String sql, Object[] params) throws SQLException {
        //预编译的sql,在后面直接执行即可
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            //setObject占位符从1开始，但是我们的数组是从0开始的
            preparedStatement.setObject(i+1,params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }


    //  编写增删改公共方法
    public static int execute(Connection connection,PreparedStatement preparedStatement,String sql,Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1,params[i]);
        }

        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
    }

    //释放资源
    public static void closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet){
        boolean flag = true;

        if(resultSet!=null){
            try {
                resultSet.close();
                //垃圾回收
                resultSet = null;
            }catch (SQLException e){
                e.printStackTrace();
                flag = false;
            }
        }
        if(preparedStatement!=null){
            try {
                preparedStatement.close();
                preparedStatement = null;
            }catch (SQLException e){
                e.printStackTrace();
                flag = false;
            }
        }
        if(connection!=null){
            try {
                connection.close();
                connection = null;
            }catch (SQLException e){
                e.printStackTrace();
                flag = false;
            }
        }
    }
}
