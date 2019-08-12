package com.unique.java.client.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.unique.java.util.CommUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/*
获取数据源、获取连接
关闭资源
 */
public class BaseDao {
    private static DruidDataSource druidDataSource;
    static {
        Properties properties = CommUtils.loadProperties("datasource.properties");
        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.out.println("数据源加载失败");
            e.printStackTrace();
        }
    }
    //继承权限protected
    protected Connection getConnection(){
        try {
            return druidDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("获取连接失败");
            e.printStackTrace();
        }
        return null;
    }
    protected void Close(Connection connection, PreparedStatement statement){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    protected void Close(Connection connection,PreparedStatement statement,ResultSet resultSet){
        if (connection != null && statement != null){
            Close(connection,statement);
        }
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
