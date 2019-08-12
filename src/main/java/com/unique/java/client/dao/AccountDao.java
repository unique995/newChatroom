package com.unique.java.client.dao;

import com.unique.java.client.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

public class AccountDao extends BaseDao{
    //用户注册insert
    public boolean userReg(User user){
        Connection connection = null;
        PreparedStatement statement = null;
        connection = getConnection();
        String sql = "insert into user(username, password, brief) values (?,?,?)";
        try {
            statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,user.getUsername());
            statement.setString(2,DigestUtils.md5Hex(user.getPassword()));
            statement.setString(3,user.getBrief());
            int rows = statement.executeUpdate();
            if (rows == 1)
                return true;
        } catch (SQLException e) {
            System.out.println("用户注册失败");
            e.printStackTrace();
        }finally {
            Close(connection,statement);
        }
        return false;
    }

    /*
    查询即登录
    都要自己建立连接，因为事务必须是隔离的
     */
    public User userLogin(String username,String password){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        connection = getConnection();
        String sql = "select * from user where username = ? and password = ?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1,username);
            statement.setString(2,DigestUtils.md5Hex(password));
            resultSet = statement.executeQuery();
            while (resultSet.next()){
               User user = getUser(resultSet);
               return user;
            }
        } catch (SQLException e) {
            System.out.println("用户登录失败");
            e.printStackTrace();
        }finally {
            Close(connection,statement,resultSet);
        }
        return null;
    }

    /*
    结果集转字符串
     */

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setBrief(resultSet.getString("brief"));
        return user;
    }
}
