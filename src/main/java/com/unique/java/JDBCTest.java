package com.unique.java;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.unique.java.util.CommUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

public class JDBCTest {
    private static DruidDataSource dataSource;
    static {
        Properties properties = CommUtils.loadProperties("datasourse.properties");
        try {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void TestSearch(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            try {
                String sql = "select * from user where username='test'";
                connection = (Connection) dataSource.getPooledConnection();
                statement = connection.prepareStatement(sql);
                resultSet = statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }finally {
            Close(connection,statement,resultSet);
        }
    }
    public static void TestInsert(){
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            try {
                connection = (Connection) dataSource.getPooledConnection();
                String sql = " insert into user(username, password, brief) values (?,?,?)";
                System.out.println(sql);
                statement = connection.prepareStatement(sql);
                statement.setString(1,"test");
                statement.setString(2,"123456");
                statement.setString(3,"cool");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            Close(connection,statement);
        }
    }
    public static void Close(Connection connection,PreparedStatement statement,ResultSet resultSet){
        if (connection != null || statement != null){
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
    public static void Close(Connection connection,PreparedStatement statement){
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

    public static void main(String[] args) {
        JDBCTest jdbcTest = new JDBCTest();
        jdbcTest.TestInsert();

    }
}
